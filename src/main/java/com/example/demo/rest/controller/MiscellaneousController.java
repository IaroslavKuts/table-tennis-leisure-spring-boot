package com.example.demo.rest.controller;

import com.example.demo.service.WorkDayScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/miscellaneous/")
public class MiscellaneousController {

    private final WorkDayScheduleService workDayScheduleService;


    @GetMapping("filteredTimePeriodsOfWorkDayForUser")
    public ResponseEntity timePeriodsOfWorkDayForUser(@RequestParam String dateOfGame){
        return ResponseEntity.ok(this.workDayScheduleService.readFilteredTimePeriodsOfWorkDayForUser(dateOfGame));

    }
    @GetMapping("filteredTimePeriodsOfWorkDayForAdmin")
    public ResponseEntity timePeriodsLoadOfWorkDayForAdmin(@RequestParam String dateOfGame){
        return ResponseEntity.ok(this.workDayScheduleService.readFilteredTimePeriodsOfWorkDayForAdmin(dateOfGame));

    }
}
