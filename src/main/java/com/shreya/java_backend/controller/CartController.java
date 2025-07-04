package com.shreya.java_backend.controller;

import com.shreya.java_backend.model.CartItem;
import com.shreya.java_backend.model.Product;
import com.shreya.java_backend.repository.ProductRepository;
import com.shreya.java_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final Map<String, List<CartItem>> userCarts = new HashMap<>();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Long> payload, @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.replace("Bearer ", "").trim());
        Long productId = payload.get("productId");

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid product ID");
        }

        Product product = optionalProduct.get();

        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setName(product.getName());
        item.setPrice(product.getPrice());
        item.setQuantity(1);

        userCarts.computeIfAbsent(email, k -> new ArrayList<>()).add(item);
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.replace("Bearer ", "").trim());
        List<CartItem> cart = userCarts.getOrDefault(email, new ArrayList<>());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable Long productId, @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.replace("Bearer ", "").trim());
        List<CartItem> cart = userCarts.getOrDefault(email, new ArrayList<>());
        cart.removeIf(item -> item.getProductId().equals(productId));
        return ResponseEntity.ok("Item removed");
    }
}
