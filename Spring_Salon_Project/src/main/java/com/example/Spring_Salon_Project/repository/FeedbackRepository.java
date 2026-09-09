package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.*;
import com.example.Spring_Salon_Project.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.FeedbackDTO(
          f.feedbackId,
          c.customerId,
          c.customerName,
          f.rating,
          f.comments,
          f.feedbackDate
      )
      FROM Feedback f LEFT JOIN f.customer c WHERE c.customerId =:customerId
      """)
    List<FeedbackDTO> findByCustomer_CustomerId(@Param("customerId") Long customerId);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.FeedbackDTO(
          f.feedbackId,
          c.customerId,
          c.customerName,
          f.rating,
          f.comments,
          f.feedbackDate
      )
      FROM Feedback f LEFT JOIN f.customer c WHERE c.customerName =:customerName
      """)
    List<FeedbackDTO> findByCustomer_CustomerName(String customerName);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.FeedbackDTO(
          f.feedbackId,
          c.customerId,
          c.customerName,
          f.rating,
          f.comments,
          f.feedbackDate
      )
      FROM Feedback f LEFT JOIN f.customer c ORDER BY f.feedbackId DESC 
      """)
    List<FeedbackDTO> getAllFeedbacks();

//    @Query("""
//      SELECT NEW com.example.Spring_Salon_Project.dto.FeedbackDTO(
//          f.feedbackId,
//          c.customerId,
//          c.customerName,
//          f.rating,
//          f.comments,
//          f.feedbackDate
//      )
//      FROM Feedback f LEFT JOIN f.customer c
//      WHERE LOWER(c.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))
//      """)
//    List<FeedbackDTO> findByCustomer_CustomerNameContainingIgnoreCase(@Param("customerName") String customerName);

    List<Feedback> findByRating(Integer rating);
}
