package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AuditLogDTO(
            a.auditLogId,
            a.action,
            a.entityName,
            a.entityId,
            a.performedBy,
            a.details,
            a.timestamp
        )
        FROM AuditLog a
        WHERE a.performedBy = :performedBy
        ORDER BY a.timestamp DESC
    """)
    List<AuditLogDTO> findByPerformedByOrderByTimestampDesc(@Param("performedBy") String performedBy);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AuditLogDTO(
            a.auditLogId,
            a.action,
            a.entityName,
            a.entityId,
            a.performedBy,
            a.details,
            a.timestamp
        )
        FROM AuditLog a
        WHERE a.entityName = :entityName
        ORDER BY a.timestamp DESC
    """)
    List<AuditLogDTO> findByEntityNameOrderByTimestampDesc(@Param("entityName") String entityName);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AuditLogDTO(
            a.auditLogId,
            a.action,
            a.entityName,
            a.entityId,
            a.performedBy,
            a.details,
            a.timestamp
        )
        FROM AuditLog a
        ORDER BY a.auditLogId DESC
    """)
    List<AuditLogDTO> getAllAuditLogs();
}