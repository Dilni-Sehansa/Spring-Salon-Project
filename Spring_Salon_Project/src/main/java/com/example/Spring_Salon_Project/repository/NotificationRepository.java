package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserUserIdOrderByCreateTimeDesc(Long userId);

    List<Notification> findByUserUserIdAndIsReadFalseOrderByCreateTimeDesc(Long userId);

    long countByUserUserIdAndIsReadFalse(Long userId);
}