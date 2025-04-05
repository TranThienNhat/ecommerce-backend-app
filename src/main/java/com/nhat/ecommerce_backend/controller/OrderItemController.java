package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.entity.OrderItem;
import com.nhat.ecommerce_backend.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-items/")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderItemService.getOrderItemsByOrderId(orderId));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getOrderItemById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(orderItemService.getOrderItemById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable UUID id) {
        try {
            orderItemService.deleteOrderItem(id);
            return ResponseEntity.ok("Xóa orderitem thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
