package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.order.OrdersRequest;
import com.nhat.ecommerce_backend.dto.order.UpdateOrderRequest;
import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.service.order.OrderService;
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getAllOrderByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getAllOrderByUserId());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PostMapping
    public ResponseEntity<String> createOrders(@RequestBody @Valid OrdersRequest request) {
        orderService.createOrder(request);
        return ResponseEntity.ok("Order created successfully");
    }


    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrder(@PathVariable UUID orderId, @RequestBody @Valid UpdateOrderRequest request) {
        orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(request.getStatus() + " update successful");
    }
}
