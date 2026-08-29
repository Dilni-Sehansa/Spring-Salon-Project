package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.StaffServiceDTO;
import com.example.Spring_Salon_Project.entity.StaffService;
import com.example.Spring_Salon_Project.enumiration.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffServiceRepository extends JpaRepository<StaffService, Long> {

    Optional<StaffService> findByStaffStaffIdAndSaloonServiceServiceId(Long staffId, Long serviceId);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffServiceDTO(
            ss.staffServiceId,
            s.staffId,
            u.userName,
            se.serviceId,
            se.serviceName,
            ss.staffServiceStatus
        )
        FROM StaffService ss
        LEFT JOIN ss.staff s
        LEFT JOIN s.user u
        LEFT JOIN ss.saloonService se
        WHERE ss.staffServiceId = :staffServiceId
    """)
    Optional<StaffServiceDTO> selectStaffService(@Param("staffServiceId") Long staffServiceId);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffServiceDTO(
            ss.staffServiceId,
            s.staffId,
            u.userName,
            se.serviceId,
            se.serviceName,
            ss.staffServiceStatus
        )
        FROM StaffService ss
        LEFT JOIN ss.staff s
        LEFT JOIN s.user u
        LEFT JOIN ss.saloonService se
        ORDER BY ss.staffServiceId DESC
    """)
    List<StaffServiceDTO> getAllStaffServices();

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffServiceDTO(
            ss.staffServiceId,
            s.staffId,
            u.userName,
            se.serviceId,
            se.serviceName,
            ss.staffServiceStatus
        )
        FROM StaffService ss
        LEFT JOIN ss.staff s
        LEFT JOIN s.user u
        LEFT JOIN ss.saloonService se
        WHERE (:staffServiceId = 0L OR ss.staffServiceId = :staffServiceId)
          AND (:staffName IS NULL OR :staffName = '' OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :staffName, '%')))
          AND (:serviceName IS NULL OR :serviceName = '' OR LOWER(se.serviceName) LIKE LOWER(CONCAT('%', :serviceName, '%')))
          AND (:status IS NULL OR ss.staffServiceStatus = :status)
        ORDER BY ss.staffServiceId DESC
    """)
    List<StaffServiceDTO> filterStaffServices(
            @Param("staffServiceId") long staffServiceId,
            @Param("staffName") String staffName,
            @Param("serviceName") String serviceName,
            @Param("status") Status status
    );

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffServiceDTO(
            ss.staffServiceId,
            s.staffId,
            u.userName,
            se.serviceId,
            se.serviceName,
            ss.staffServiceStatus
        )
        FROM StaffService ss
        LEFT JOIN ss.staff s
        LEFT JOIN s.user u
        LEFT JOIN ss.saloonService se
        WHERE s.staffId = :staffId
    """)
    List<StaffServiceDTO> getStaffServicesByStaffId(@Param("staffId") Long staffId);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.StaffServiceDTO(
            ss.staffServiceId,
            s.staffId,
            u.userName,
            se.serviceId,
            se.serviceName,
            ss.staffServiceStatus
        )
        FROM StaffService ss
        LEFT JOIN ss.staff s
        LEFT JOIN s.user u
        LEFT JOIN ss.saloonService se
        WHERE se.serviceId = :serviceId
    """)
    List<StaffServiceDTO> getStaffServicesByServiceId(@Param("serviceId") Long serviceId);
}