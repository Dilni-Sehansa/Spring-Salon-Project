package com.example.Spring_Salon_Project.exception;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public CommonResponse handleServerException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new CommonResponse(500, "UNEXPECTED_ERROR");
    }

    @ExceptionHandler(value = {CustomerException.class})
    public ResponseEntity<CommonResponse> handleCustomerException(CustomerException ex, WebRequest webRequest){
        ex.printStackTrace();

        return ResponseEntity.ok(new CommonResponse(ex.getStatus(), ex.getMessage()));
    }
}
