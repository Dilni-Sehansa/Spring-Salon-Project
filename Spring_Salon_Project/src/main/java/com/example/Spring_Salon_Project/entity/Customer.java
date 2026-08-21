package com.example.Spring_Salon_Project.entity;

import com.example.Spring_Salon_Project.enumiration.CustomerStatus;
import com.example.Spring_Salon_Project.enumiration.UserStatus;
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
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id" )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus customerStatus;
}
