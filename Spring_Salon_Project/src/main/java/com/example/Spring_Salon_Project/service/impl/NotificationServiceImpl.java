package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.NotificationDTO;
import com.example.Spring_Salon_Project.entity.Notification;
import com.example.Spring_Salon_Project.entity.User;
import com.example.Spring_Salon_Project.enumiration.NotificationType;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.NotificationRepository;
import com.example.Spring_Salon_Project.repository.UserRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import com.example.Spring_Salon_Project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    private NotificationDTO convertToDTO(Notification saved) {
        Long savedUserId = (saved.getUser() != null) ? saved.getUser().getUserId() : null;
        String savedUserName = (saved.getUser() != null) ? saved.getUser().getUserName() : null;

        return new NotificationDTO(
                saved.getNotificationId(),
                savedUserId,
                savedUserName,
                saved.getTitle(),
                saved.getMessage(),
                saved.getType(),
                saved.isRead(),
                saved.getCreateTime()
        );
    }

    @Override
    @Transactional
    public NotificationDTO createNotification(Long userId, String title, String message, NotificationType type) {
        log.info("Execute method createNotification for userId: {}", userId);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new CustomerException(404, "User not found ID: " + userId);
        }

        User user = optionalUser.get();

        Notification notification = Notification.builder()
                .user(optionalUser.get())
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Notification created successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("CREATE");
        logDTO.setEntityName("NOTIFICATION");
        logDTO.setEntityId(saved.getNotificationId());
        logDTO.setPerformedBy(user.getUserName() != null ? user.getUserName() : "admin");
        logDTO.setDetails("New Notification created for User: " + user.getUserName() + ", Title: " + saved.getTitle());
        auditLogService.saveAuditLog(logDTO);

        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications(Long userId) {
        log.info("Execute method getUserNotifications for userId: {}", userId);

        List<Notification> notificationList = notificationRepository.findByUserUserIdOrderByCreateTimeDesc(userId);
        List<NotificationDTO> dtoList = new ArrayList<>();

        for (Notification notification : notificationList) {
            dtoList.add(convertToDTO(notification));
        }
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        log.info("Execute method getUnreadNotifications for userId: {}", userId);

        List<Notification> unreadList = notificationRepository.findByUserUserIdAndIsReadFalseOrderByCreateTimeDesc(userId);
        List<NotificationDTO> dtoList = new ArrayList<>();

        for (Notification notification : unreadList) {
            dtoList.add(convertToDTO(notification));
        }
        return dtoList;
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        log.info("Execute method markAsRead for notificationId: {}", notificationId);

        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isEmpty()) {
            throw new CustomerException(404, "Notification not found ID: " + notificationId);
        }

        Notification notification = optionalNotification.get();
        notification.setRead(true);
        notificationRepository.save(notification);
        log.info("Notification marked as read successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("NOTIFICATION");
        logDTO.setEntityId(notification.getNotificationId());
        logDTO.setPerformedBy(notification.getUser() != null ? notification.getUser().getUserName() : "admin");
        logDTO.setDetails("Notification marked as read ID: " + notification.getNotificationId());
        auditLogService.saveAuditLog(logDTO);


    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        log.info("Execute method markAllAsRead for userId: {}", userId);

        List<Notification> unreadList = notificationRepository.findByUserUserIdAndIsReadFalseOrderByCreateTimeDesc(userId);

        if (unreadList != null && !unreadList.isEmpty()) {
            for (Notification notification : unreadList) {
                notification.setRead(true);
            }
            notificationRepository.saveAll(unreadList);
            log.info("All notifications marked as read successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("UPDATE");
            logDTO.setEntityName("NOTIFICATION");
            logDTO.setEntityId(userId);
            logDTO.setPerformedBy("user_" + userId);
            logDTO.setDetails("Marked all notifications as read for User ID: " + userId);
            auditLogService.saveAuditLog(logDTO);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        log.info("Execute method getUnreadCount for userId: {}", userId);
        return notificationRepository.countByUserUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        log.info("Execute method deleteNotification for notificationId: {}", notificationId);

        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isEmpty()) {
            throw new CustomerException(404, "Notification not found ID: " + notificationId);
        }

        Notification notification = optionalNotification.get();
        notificationRepository.deleteById(notificationId);
        log.info("Notification deleted successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("DELETE");
        logDTO.setEntityName("NOTIFICATION");
        logDTO.setEntityId(notification.getNotificationId());
        logDTO.setPerformedBy(notification.getUser() != null ? notification.getUser().getUserName() : "admin");
        logDTO.setDetails("Notification deleted ID: " + notification.getNotificationId());
        auditLogService.saveAuditLog(logDTO);

//        log.info("Execute method deleteNotification() notificationId: {}", notificationId);
//
//        try{
//            Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
//
//            if(optionalNotification.isEmpty() || optionalNotification.get().getNotificationStatus()==NotificationStatus.INACTIVE){
//                throw new CustomerException(404, "Notification not found");
//            }
//
//            Notification notification = optionalNotification.get();
//                notification.setNotificationStatus(NotificationStatus.INACTIVE);
//            notificationRepository.save(notification);
//        } catch (Exception e) {
//            log.error("Error delete staff");
//            throw e;
//        }
    }


}