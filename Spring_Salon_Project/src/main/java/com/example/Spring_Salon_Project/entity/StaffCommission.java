package com.example.Spring_Salon_Project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class StaffCommission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commissionId;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "appointment_detail_id", nullable = false)
    private AppointmentDetail appointmentDetail;

    @Column(nullable = false)
    private Double commissionRate;

    @Column(nullable = false)
    private Double commissionAmount;

    @Column(nullable = false)
    private LocalDate commissionDate;
}
