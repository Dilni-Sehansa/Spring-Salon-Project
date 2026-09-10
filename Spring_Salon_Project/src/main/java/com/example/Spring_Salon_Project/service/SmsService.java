package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.SmsRequest;

public interface SmsService {
    void sendSms(SmsRequest smsRequest);
}
