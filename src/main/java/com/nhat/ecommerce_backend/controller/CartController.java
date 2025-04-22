package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.cart.CartItemResponse;
import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItem() {
        List<CartItemResponse> cartItems = cartService.getCartItems();
        return ResponseEntity.ok(cartItems);
    }
}


