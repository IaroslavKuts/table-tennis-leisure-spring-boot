package com.example.demo.rest.controller;

import com.example.demo.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/paypal/")
public class PayPalController {
    private  final PayPalService payPalService;
    @PostMapping("create-appOrder")
    public ResponseEntity createOrder(@RequestBody ArrayList<String> chosenTimePeriods) throws IOException {

        return ResponseEntity.ok(payPalService.createOrder(chosenTimePeriods));
    }
}
