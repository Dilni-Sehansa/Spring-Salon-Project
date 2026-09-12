package com.example.Spring_Salon_Project.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String upload(MultipartFile file);
}
