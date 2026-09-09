package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.AppointmentDetailDTO;
import com.example.Spring_Salon_Project.entity.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentDetailRepository extends JpaRepository<AppointmentDetail, Long> {

    List<AppointmentDetail> findByAppointment_AppointmentId(Long appointmentId);

    @Query("SELECT new com.example.Spring_Salon_Project.dto.AppointmentDetailDTO(" +
            "ad.appointmentServiceId, ad.price, a.appointmentId, s.serviceId, s.serviceName, ad.deleted) " +
            "FROM AppointmentDetail ad " +
            "LEFT JOIN ad.appointment a " +
            "LEFT JOIN ad.service s " +
            "WHERE a.appointmentId = :appointmentId")
    List<AppointmentDetailDTO> getDetailsByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AppointmentDetailDTO(
            ad.appointmentServiceId, ad.price, a.appointmentId, s.serviceId, s.serviceName, ad.deleted)
        FROM AppointmentDetail ad
        LEFT JOIN ad.appointment a
        LEFT JOIN a.customer c
        LEFT JOIN ad.service s
        WHERE (:phone IS NULL OR :phone = '' OR c.phone LIKE CONCAT('%', :phone, '%'))
          AND (:customerName IS NULL OR :customerName = '' OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :customerName, '%')))
        ORDER BY ad.appointmentServiceId DESC
        """)
    List<AppointmentDetailDTO> getAppointmentDetailsByPhoneAndCustomerName(
            @Param("phone") String phone,
            @Param("customerName") String customerName
    );

    @Query("SELECT new com.example.Spring_Salon_Project.dto.AppointmentDetailDTO(" +
            "ad.appointmentServiceId, ad.price, a.appointmentId, s.serviceId, s.serviceName, ad.deleted) " +
            "FROM AppointmentDetail ad " +
            "LEFT JOIN ad.appointment a " +
            "LEFT JOIN ad.service s " +
            "WHERE ad.appointmentServiceId = :id")
    AppointmentDetailDTO getAppointmentDetailById(@Param("id") Long id);
}