package com.example.demo.service;

import com.example.demo.DTO.IWorkDaysOpenCloseTime;
import com.example.demo.domain.entity.*;
import com.example.demo.repository.AlteredWorkScheduleRepository;
import com.example.demo.repository.WorkScheduleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkDayScheduleService {
    private static Logger LOG = LoggerFactory.getLogger(WorkDayScheduleService.class);
    private final WorkScheduleRepository workScheduleRepository;
    private final AlteredWorkScheduleRepository alteredWorkScheduleRepository;
    private final EntityManager entityManager;

    @Value("${application.tables}")
    private String amountOfTables;

    private IWorkDaysOpenCloseTime readWorkDaySchedule(String dateOfGame){
        IWorkDaysOpenCloseTime scheduleTime;
        Optional<IWorkDaysOpenCloseTime> alteredWorkScheduleOfCertainDay = this.alteredWorkScheduleRepository.findByDateEquals(dateOfGame);
        if(alteredWorkScheduleOfCertainDay.isPresent()) {
                scheduleTime = alteredWorkScheduleOfCertainDay.get();
        }else {
            Integer day_id = LocalDate.parse(dateOfGame).getDayOfWeek().getValue();
            day_id = day_id == 7? 0: day_id;
            Optional<WorkSchedule> basicWorkScheduleOfWeekDay = this.workScheduleRepository.findById(day_id.longValue());
            scheduleTime = basicWorkScheduleOfWeekDay.get();
        }

        return scheduleTime;
    }

    /**
     * Returns a list of time periods with interval of 30 minutes. Map contains two keys: start_time, end_time
     * Example:
     * Work day starts at 15:00 and ends at 22:00
     * It will result in
     * [{"start_time":"15:00","end_time":"15:30"},{"start_time":"15:30","end_time":"16:00"}, ... ,{"start_time":"21:30","end_time":"22:00"}]
     *
     * @param dateOfGame - date of game in YYYY-MM-DD format
     * @return List of Map filled with time periods
     *
     */
    private List<Map<String,String>> timePeriodsOfWorkDay(String dateOfGame){
        IWorkDaysOpenCloseTime openCloseTime = readWorkDaySchedule(dateOfGame);
        List<Map<String,String>> result = new ArrayList<>();
        if(openCloseTime.getOpen().equals("-----"))
            return  result;
        Map<String,String> timePeriodsMap;
        LocalTime startTime = LocalTime.parse(openCloseTime.getOpen());
        LocalTime endTime = startTime.plusMinutes(30L);
        LocalTime finalTime = LocalTime.parse(openCloseTime.getClose());

        do{
            timePeriodsMap = new HashMap<>();
            timePeriodsMap.put("start_time",startTime.toString());
            timePeriodsMap.put("end_time",endTime.toString());
            result.add(timePeriodsMap);
            startTime = startTime.plusMinutes(30L);
            endTime = endTime.plusMinutes(30L);

        }while (endTime.isBefore(finalTime));
        timePeriodsMap = new HashMap<>();
        timePeriodsMap.put("start_time",startTime.toString());
        timePeriodsMap.put("end_time",endTime.toString());
        result.add(timePeriodsMap);
        return result;
    }

    public List<Map<String,String>> readFilteredTimePeriodsOfWorkDayForUser(String dateOfGame){
        List<Map<String,String>> timePeriodsOfWorkDay = timePeriodsOfWorkDay(dateOfGame);
        List<Pair> timePeriodsLoadOfWorkDay = timePeriodsLoadOfWorkDay(dateOfGame);
        timePeriodsOfWorkDay.forEach(timePeriods->{

            Boolean isBlocked = timePeriodsLoadOfWorkDay.stream().anyMatch(el->
                    timePeriods.get("start_time").equals(el.getFirst()) && el.getSecond().equals(Long.valueOf(amountOfTables))
            );
            timePeriods.put("blocked",String.valueOf(isBlocked));
        });
        return timePeriodsOfWorkDay;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<Map<String,String>> readFilteredTimePeriodsOfWorkDayForAdmin(String dateOfGame){
        List<Pair> timePeriodsLoadOfWorkDay = timePeriodsLoadOfWorkDay(dateOfGame);
        List<Map<String,String>> timePeriodsOfWorkDayForAdmin =  readFilteredTimePeriodsOfWorkDayForUser(dateOfGame);
        timePeriodsOfWorkDayForAdmin.forEach(timePeriods->{
            if(Boolean.valueOf(timePeriods.get("blocked")))
                timePeriods.put("amount",amountOfTables);
            else{
                timePeriods.put("amount","0");
                timePeriodsLoadOfWorkDay.forEach(el->{
                        if(timePeriods.get("start_time").equals(el.getFirst()))
                            timePeriods.put("amount",String.valueOf(el.getSecond()));

                }
                );
            }
        });
        timePeriodsOfWorkDayForAdmin.forEach(System.out::println);
        return timePeriodsOfWorkDayForAdmin;
    }




    private List<Pair> timePeriodsLoadOfWorkDay(String dateOfGame){
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery cq = cb
            .createQuery(Pair.class);
    Root<AppOrder> root = cq.from(AppOrder.class);
    CompoundSelection cmpd =  cb.construct(
            Pair.class,
            root.get(AppOrder_.START_TIME),
            cb.count(root.get(AppOrder_.START_TIME))
    );
    cq.select(cmpd);
    cq.where(cb.equal(root.get(AppOrder_.DATE_OF_GAME),dateOfGame));
    cq.groupBy(root.get(AppOrder_.START_TIME));

    TypedQuery<Pair> tq = entityManager.createQuery(cq);
    List<Pair> resultList = tq.getResultList();
    return resultList;
}

}
