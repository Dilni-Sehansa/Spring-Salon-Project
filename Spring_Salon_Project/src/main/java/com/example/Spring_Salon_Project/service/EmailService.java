package com.example.Spring_Salon_Project.service;

public interface EmailService {
    void sendWelcomeEmail(String toEmail, String userName, String plainPassword);
    void sendVerificationEmail(String toEmail, String userName, String plainPassword, String verificationUrl);
}