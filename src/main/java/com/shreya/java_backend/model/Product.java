package com.shreya.java_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")  
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double price;

    private String imageUrl;  
}

