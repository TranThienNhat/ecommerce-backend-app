package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.CartRequest;
import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.service.CartService;
import com.nhat.ecommerce_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCartForUser(@PathVariable Long userId) {
        return cartService.getCartForUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


