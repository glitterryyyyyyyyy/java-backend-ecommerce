package com.shreya.java_backend.controller;

import com.shreya.java_backend.model.CartItem;
import com.shreya.java_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private JwtUtil jwtUtil;

    private final Map<String, List<CartItem>> userCarts = new HashMap<>();

    // ========== Add to Cart ==========
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody CartItem item,
            @RequestHeader("Authorization") String authHeader) {
        String email = extractEmail(authHeader);
        userCarts.computeIfAbsent(email, k -> new ArrayList<>()).add(item);
        return ResponseEntity.ok("Item added to cart");
    }

    // ========== Get Cart ==========
    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String authHeader) {
        String email = extractEmail(authHeader);
        List<CartItem> cart = userCarts.getOrDefault(email, new ArrayList<>());
        return ResponseEntity.ok(cart);
    }

    // ========== Remove Item ==========
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String authHeader) {
        String email = extractEmail(authHeader);
        List<CartItem> cart = userCarts.getOrDefault(email, new ArrayList<>());
        cart.removeIf(item -> item.getProductId().equals(productId));
        return ResponseEntity.ok("Item removed");
    }

    // ========== Helper: Extract Email from Token ==========
    private String extractEmail(String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        return jwtUtil.extractUsername(token);
    }
}
