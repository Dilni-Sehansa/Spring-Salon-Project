package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.UserRole;
import com.example.Spring_Salon_Project.enumiration.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long userId;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String userName;
    private UserRole userRole;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    private UserStatus userStatus;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    public UserDTO(Long userId,String userName, UserRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
    }
}
