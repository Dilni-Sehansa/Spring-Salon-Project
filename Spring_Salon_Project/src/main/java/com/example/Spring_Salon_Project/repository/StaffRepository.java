package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.StaffDTO;
import com.example.Spring_Salon_Project.entity.Staff;
import com.example.Spring_Salon_Project.enumiration.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Long> {
    @Query("""
         SELECT new com.example.Spring_Salon_Project.dto.StaffDTO(
              s.staffId,
              s.specialization,
              s.bio,
              s.experienceYears,
               u.userId,
              s.staffStatus
            
         )
         FROM Staff s LEFT JOIN s.user u WHERE u.userId = :userId
         """)
    Optional<StaffDTO> getStaffByUserId(@Param("userId") long userId);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.StaffDTO(
          s.staffId,
          s.specialization,
          s.bio,
          s.experienceYears,
          u.userId,
          s.staffStatus
      )
      FROM Staff s LEFT JOIN s.user u ORDER BY s.staffId DESC 
      """)
    List<StaffDTO> getAllStaff();

    @Query("""
          SELECT new com.example.Spring_Salon_Project.dto.StaffDTO(
               s.staffId,
               s.specialization,
               s.bio,
               s.experienceYears,
               u.userId,
               s.staffStatus
          )
          FROM Staff s LEFT JOIN s.user u WHERE s.staffStatus = :staffStatus
         """)
    List<StaffDTO> findByStaffStatus(@Param("staffStatus") StaffStatus staffStatus);

    @Query("""
     SELECT new com.example.Spring_Salon_Project.dto.StaffDTO(
          s.staffId,
          s.specialization,
          s.bio,
          s.experienceYears,
          u.userId,
          s.staffStatus
     )
     FROM Staff s LEFT JOIN s.user u WHERE s.staffId = :staffId
     """)
    StaffDTO selectStaff(@Param("staffId") Long staffId);

    @Query("""
    SELECT new com.example.Spring_Salon_Project.dto.StaffDTO(
        s.staffId,
        s.specialization,
        s.bio,
        s.experienceYears,
        u.userId,
        s.staffStatus
    )
    FROM Staff s
    LEFT JOIN s.user u
    WHERE :searchValue IS NULL 
       OR :searchValue = ''
       OR LOWER(s.specialization) LIKE LOWER(CONCAT('%', :searchValue, '%'))
       OR LOWER(s.bio) LIKE LOWER(CONCAT('%', :searchValue, '%'))
       OR LOWER(CAST(s.staffStatus AS string)) LIKE LOWER(CONCAT('%', :searchValue, '%'))
    ORDER BY s.staffId DESC
    """)
    List<StaffDTO> filterStaff(@Param("searchValue") String searchValue);
}
