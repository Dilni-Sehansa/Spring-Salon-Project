package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.UserRole;
import com.example.Spring_Salon_Project.enumiration.UserStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private String userName;
    private UserRole userRole;
    private String password;
    private UserStatus userStatus;
    private String email;

    public UserDTO(Long userId,String userName, UserRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
    }
}
