package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/auditLog")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService auditLogService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/auditLog-saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveAuditLog(@RequestBody AuditLogDTO auditLogDTO){
        AuditLogDTO saveAuditLog = auditLogService.saveAuditLog(auditLogDTO);
        return new CommonResponse(0,saveAuditLog,"AuditLog Saved Successfully");
    }

    @GetMapping(value = "/auditLog", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllAuditLogs(){
        List<AuditLogDTO> getAuditLog = auditLogService.getAllAuditLogs();
        return new CommonResponse(0,getAuditLog,"AuditLog Loaded Successfully");
    }

    @GetMapping(value = "/by-user/{performedBy}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAuditLogsByPerformedBy(@PathVariable String performedBy) {
        List<AuditLogDTO> list = auditLogService.getAuditLogsByPerformedBy(performedBy);
        return new CommonResponse(0, list, "AuditLog Loaded Successfully");
    }

    @GetMapping(value = "/by-entity/{entityName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAuditLogsByEntityName(@PathVariable String entityName) {
        List<AuditLogDTO> list = auditLogService.getAuditLogsByEntityName(entityName);
        return new CommonResponse(0, list, "AuditLog Loaded Successfully");
    }
}
