package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.NotificationDTO;
import com.example.Spring_Salon_Project.enumiration.NotificationType;

import java.util.List;

public interface NotificationService {

    NotificationDTO createNotification(Long userId, String title, String message, NotificationType type);
    List<NotificationDTO> getUserNotifications(Long userId);
    List<NotificationDTO> getUnreadNotifications(Long userId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    long getUnreadCount(Long userId);
    void deleteNotification(Long notificationId);
    List<NotificationDTO> getAllNotifications();
}