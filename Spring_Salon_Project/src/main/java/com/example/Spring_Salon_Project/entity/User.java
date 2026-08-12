package com.example.Spring_Salon_Project.entity;


import com.example.Spring_Salon_Project.enumiration.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

}
