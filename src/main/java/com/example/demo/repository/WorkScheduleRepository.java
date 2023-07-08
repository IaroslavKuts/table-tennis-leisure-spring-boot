package com.example.demo.repository;

import com.example.demo.domain.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "work_schedule",
            path="work_schedule")
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule,Long> {
    @Query("SELECT day_id from work_schedule where open = '-----'")
    List<Long> findBlockedWeekdays();

}
