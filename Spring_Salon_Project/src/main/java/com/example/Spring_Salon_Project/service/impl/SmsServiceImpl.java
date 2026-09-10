package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.SmsRequest;
import com.example.Spring_Salon_Project.service.SmsService;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Value("${vonage.api.key}")
    private String apiKey;

    @Value("${vonage.api.secret}")
    private String apiSecret;

    @Value("${vonage.brand.name}")
    private String brandName;

    @Override
    public void sendSms(SmsRequest smsRequest) {
        try {
            VonageClient client = VonageClient.builder()
                    .apiKey(apiKey)
                    .apiSecret(apiSecret)
                    .build();

            TextMessage message = new TextMessage(
                    brandName.trim(),
                    smsRequest.getPhoneNumber(),
                    smsRequest.getMessage()
            );

            SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                LOGGER.info("SMS sent successfully to: {}", smsRequest.getPhoneNumber());
            } else {
                String error = response.getMessages().get(0).getErrorText();
                LOGGER.error("SMS failed to {}: {}", smsRequest.getPhoneNumber(), error);
                throw new RuntimeException("SMS failed: " + error);
            }
        } catch (Exception e) {
            LOGGER.error("Error sending SMS to {}: {}", smsRequest.getPhoneNumber(), e.getMessage());
            throw new RuntimeException("Error sending SMS: " + e.getMessage());
        }
    }
}