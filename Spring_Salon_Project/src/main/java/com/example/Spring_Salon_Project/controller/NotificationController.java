package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.NotificationDTO;
import com.example.Spring_Salon_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping(value = "/save-notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createNotification(@RequestBody NotificationDTO notificationDTO) {
        NotificationDTO savedNotification = notificationService.createNotification(
                notificationDTO.getUserId(),
                notificationDTO.getTitle(),
                notificationDTO.getMessage(),
                notificationDTO.getType()
        );
        return new CommonResponse(0, savedNotification, "Notification Saved Successfully");
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getUserNotifications(userId);
        return new CommonResponse(0, notifications, "User Notifications Loaded Successfully");
    }

    @GetMapping(value = "/user/{userId}/unread", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUnreadNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(userId);
        return new CommonResponse(0, notifications, "Unread Notifications Loaded Successfully");
    }

    @GetMapping(value = "/user/{userId}/unread-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return new CommonResponse(0, count, "Unread Count Loaded Successfully");
    }

    @PatchMapping(value = "/read/{notificationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return new CommonResponse(0, "Notification Marked as Read Successfully");
    }

    @PatchMapping(value = "/user/{userId}/read-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return new CommonResponse(0, "All Notifications Marked as Read Successfully");
    }

    @DeleteMapping(value = "/{notificationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return new CommonResponse(0, "Notification Deleted Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return new CommonResponse(0, notifications, "All Notifications Loaded Successfully");
    }
}