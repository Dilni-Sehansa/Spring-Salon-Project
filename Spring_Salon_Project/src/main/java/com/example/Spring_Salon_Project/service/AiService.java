package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.AiStyleDnaResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AiService {
    AiStyleDnaResponseDTO analyzeStyleDna(MultipartFile image, Long customerId, String preferredStyle);
}