package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.OrdersRequest;
import com.nhat.ecommerce_backend.dto.PlaceOrderRequest;
import com.nhat.ecommerce_backend.dto.UpdateOrderRequest;
import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrders(@RequestBody @Valid OrdersRequest request) {
        try {
            Order order = orderService.createOrder(request);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getAllOrderByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getAllOrderByUserId(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrder(@PathVariable UUID orderId, @RequestBody @Valid UpdateOrderRequest request) {
        try {
            return ResponseEntity.ok(orderService.updateOrder(orderId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@RequestBody @Valid PlaceOrderRequest request) {
        try {
            Order order = orderService.createPlaceOrder(request);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
