package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.SmsRequest;
import com.example.Spring_Salon_Project.service.SmsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sms")
@CrossOrigin(origins = "*")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendSms(@RequestBody SmsRequest smsRequest) {
        smsService.sendSms(smsRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("message", "SMS notification sent successfully!")
        );
    }
}