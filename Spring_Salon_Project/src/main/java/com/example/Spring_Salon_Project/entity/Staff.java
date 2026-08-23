package com.example.Spring_Salon_Project.entity;

import com.example.Spring_Salon_Project.enumiration.CustomerStatus;
import com.example.Spring_Salon_Project.enumiration.StaffStatus;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long staffId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String bio;

    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffStatus staffStatus;
    

}
