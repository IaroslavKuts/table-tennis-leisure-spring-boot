package com.example.demo.repository;

import com.example.demo.DTO.IWorkDaysOpenCloseTime;
import com.example.demo.domain.entity.AlteredWorkSchedule;
import com.example.demo.domain.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "altered_work_schedule",
        path="altered_work_schedule")
public interface AlteredWorkScheduleRepository extends JpaRepository<AlteredWorkSchedule,Long> {

    Optional<IWorkDaysOpenCloseTime> findByDateEquals(String date);
//    @Query("SELECT date from altered_work_schedule where open = '-----'")
//    List<String> findBlockedDates();

}
