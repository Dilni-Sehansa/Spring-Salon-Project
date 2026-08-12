package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.UserRole;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private long userId;
    private String userName;
    private UserRole userRole;
    private String password;


}
