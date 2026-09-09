package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;

import java.util.List;

public interface AuditLogService {

    AuditLogDTO saveAuditLog(AuditLogDTO auditLogDTO);
    List<AuditLogDTO> getAllAuditLogs();
    List<AuditLogDTO> getAuditLogsByPerformedBy(String performedBy);
    List<AuditLogDTO> getAuditLogsByEntityName(String entityName);
}
