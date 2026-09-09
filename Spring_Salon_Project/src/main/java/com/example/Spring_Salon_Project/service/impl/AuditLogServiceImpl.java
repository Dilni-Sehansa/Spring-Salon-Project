package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.entity.AuditLog;
import com.example.Spring_Salon_Project.repository.AuditLogRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public AuditLogDTO saveAuditLog(AuditLogDTO auditLogDTO) {
        log.info("Execute method saveAuditLog");
        try {

            AuditLog auditLog = new AuditLog();
            auditLog.setAction(auditLogDTO.getAction());
            auditLog.setEntityName(auditLogDTO.getEntityName());
            auditLog.setEntityId(auditLogDTO.getEntityId());
            auditLog.setPerformedBy(auditLogDTO.getPerformedBy());
            auditLog.setDetails(auditLogDTO.getDetails());

            if (auditLogDTO.getTimestamp() != null) {
                auditLog.setTimestamp(auditLogDTO.getTimestamp());
            } else {
                auditLog.setTimestamp(LocalDateTime.now());
            }

            AuditLog saved = auditLogRepository.save(auditLog);
            log.info("AuditLog saved successfully");

            return new AuditLogDTO(
                    saved.getAuditLogId(),
                    saved.getAction(),
                    saved.getEntityName(),
                    saved.getEntityId(),
                    saved.getPerformedBy(),
                    saved.getDetails(),
                    saved.getTimestamp()
            );
        } catch (Exception e) {
            log.error("Error saving AuditLog: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.getAllAuditLogs();
    }

    @Override
    public List<AuditLogDTO> getAuditLogsByPerformedBy(String performedBy) {
        log.info("Execute method getAuditLogsByPerformedBy: {}", performedBy);
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(performedBy);
    }

    @Override
    public List<AuditLogDTO> getAuditLogsByEntityName(String entityName) {
        log.info("Execute method getAuditLogsByEntityName: {}", entityName);
        return auditLogRepository.findByEntityNameOrderByTimestampDesc(entityName);
    }


}