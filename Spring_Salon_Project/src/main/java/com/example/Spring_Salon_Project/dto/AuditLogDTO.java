package com.example.Spring_Salon_Project.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditLogDTO {
    private Long auditLogId;
    private String action;
    private String entityName;
    private Long entityId;
    private String performedBy;
    private String details;
    private LocalDateTime timestamp;
}