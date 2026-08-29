package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.SaloonServiceDTO;
import com.example.Spring_Salon_Project.entity.SaloonService;
import com.example.Spring_Salon_Project.enumiration.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaloonServiceRepository extends JpaRepository<SaloonService,Long> {
    Optional<SaloonService> findByServiceName(String serviceName);

    @Query("""
         SELECT new com.example.Spring_Salon_Project.dto.SaloonServiceDTO(
              s.serviceId,
              s.serviceName,
              s.description,
              s.price,
              s.durationMinutes,
              c.categoryId,
              c.categoryName,
              s.serviceStatus
         )
         FROM SaloonService s 
         LEFT JOIN s.category c 
         WHERE s.serviceId = :saloonServiceId
         """)
    SaloonServiceDTO selectSaloonServices(@Param("saloonServiceId") Long saloonServiceId);


    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.SaloonServiceDTO(
          s.serviceId,
          s.serviceName,s.description,s.price,s.durationMinutes,c.categoryId,c.categoryName,s.serviceStatus
      )
      FROM SaloonService s LEFT JOIN s.category c ORDER BY s.serviceId DESC 
      """)
    List<SaloonServiceDTO> getAllSaloonServices();

    @Query("""
         SELECT new com.example.Spring_Salon_Project.dto.SaloonServiceDTO(
              s.serviceId,
              s.serviceName,
              s.description,
              s.price,
              s.durationMinutes,
              c.categoryId,
              c.categoryName,
              s.serviceStatus
         )
         FROM SaloonService s 
         LEFT JOIN s.category c 
         WHERE (:serviceId = 0L OR s.serviceId = :serviceId)
           AND (:serviceName IS NULL OR :serviceName = '' OR LOWER(s.serviceName) LIKE LOWER(CONCAT('%', :serviceName, '%')))
           AND (:serviceStatus IS NULL OR s.serviceStatus = :serviceStatus)
         ORDER BY s.serviceId DESC
         """)
    List<SaloonServiceDTO> searchServices(
            @Param("serviceId") long serviceId,
            @Param("serviceName") String serviceName,
            @Param("serviceStatus") ServiceStatus serviceStatus
    );


}
