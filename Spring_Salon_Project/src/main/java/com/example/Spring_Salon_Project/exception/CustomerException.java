package com.example.Spring_Salon_Project.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomerException extends RuntimeException {
    private int status;
    private String message;
}
