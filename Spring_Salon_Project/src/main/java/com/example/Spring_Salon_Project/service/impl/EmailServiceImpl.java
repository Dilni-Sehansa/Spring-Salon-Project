package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendWelcomeEmail(String toEmail, String userName, String plainPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Glow Salon!");
            message.setText(
                    "Hello " + userName + ",\n\n" +
                            "Welcome to Glow Salon Management System!\n\n" +
                            "Your account has been created successfully.\n" +
                            "Username : " + userName + "\n" +
                            "Password : " + plainPassword + "\n\n" +
                            "Please login using the link below:\n" +
                            "http://localhost:8080/login.html\n\n" +
                            "Please change your password after first login.\n\n" +
                            "Thank you,\n" +
                            "Glow Salon Team"
            );
            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }

    @Override
    public void sendVerificationEmail(String toEmail, String userName, String plainPassword, String verificationUrl) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Verify your Glow Salon account");
            message.setText(
                    "Hello " + userName + ",\n\n" +
                            "Thank you for registering with Glow Salon!\n\n" +
                            "Your account details:\n" +
                            "Username : " + userName + "\n" +
                            "Password : " + plainPassword + "\n\n" +
                            "Please click the link below to verify your email address:\n" +
                            verificationUrl + "\n\n" +
                            "This link will activate your account.\n\n" +
                            "After verification, login here:\n" +
                            "http://localhost:8080/login.html\n\n" +
                            "Please change your password after first login.\n\n" +
                            "If you did not create this account, please ignore this email.\n\n" +
                            "Thank you,\n" +
                            "Glow Salon Team"
            );
            mailSender.send(message);
            log.info("Verification email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", toEmail, e.getMessage());
        }
    }
}