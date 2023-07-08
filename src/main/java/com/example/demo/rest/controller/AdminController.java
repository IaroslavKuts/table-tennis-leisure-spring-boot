package com.example.demo.rest.controller;

import com.example.demo.service.AdminSqlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/")
public class AdminController {

    private final AdminSqlService adminSqlService;

    @GetMapping("readCustomersAbonements")
    public ResponseEntity readCustomersAbonements(){

        return ResponseEntity.ok(adminSqlService.readCustomersAbonements());

    }

    @GetMapping("readCustomersAges")
    public ResponseEntity readCustomersAges(){

        return ResponseEntity.ok(adminSqlService.readCustomersAges());

    }

    @GetMapping("readProfitBetweenDates")
    public ResponseEntity readProfitBetweenDates(@RequestParam Optional<String> start_date,@RequestParam Optional<String> end_date){
        return ResponseEntity.ok(adminSqlService.readProfitBetweenDates(start_date,end_date));

    }

    @GetMapping("readDaysLoadBetweenDays")
    public ResponseEntity readDaysLoadBetweenDays(@RequestParam Optional<String> start_date,@RequestParam Optional<String> end_date){
        return ResponseEntity.ok(adminSqlService.readDaysLoadBetweenDays(start_date,end_date));

    }

    @GetMapping("readUsersDataByPayment")
    public ResponseEntity readUsersDataByPayment(@RequestParam Optional<String> start_date,
                                             @RequestParam Optional<String> end_date,
                                             @RequestParam Optional<Integer> start_payment,
                                             @RequestParam Optional<Integer> end_payment){
        return ResponseEntity.ok(adminSqlService.readUsersDataByPayment(start_date,end_date,start_payment,end_payment));

    }
}
