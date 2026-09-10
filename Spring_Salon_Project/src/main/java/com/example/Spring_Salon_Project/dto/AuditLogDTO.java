package com.example.Spring_Salon_Project.dto;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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