package com.shreya.java_backend.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long productId;
    private String name;
    private double price;
    private int quantity;
}
