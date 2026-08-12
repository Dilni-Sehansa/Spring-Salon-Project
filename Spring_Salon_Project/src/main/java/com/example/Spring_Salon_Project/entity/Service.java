package com.example.Spring_Salon_Project.entity;


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
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;
    private String name;

    private Double price;
    private Integer durationMin;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
