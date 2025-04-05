package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.entity.*;
import com.nhat.ecommerce_backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public void createOrderItem(Order savedOrder, List<CartItem> cartItems) {
        cartItems.forEach(ci -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(ci.getProduct());
            orderItem.setPrice(ci.getProduct().getPrice());
            orderItem.setQuantity(ci.getQuantity());

            orderItemRepository.save(orderItem);
        });
    }

    public void createOrderItem(Order savedPlaceOrder, Product product, Integer quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(savedPlaceOrder);
        orderItem.setProduct(product);
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(quantity);

        // Lưu OrderItem vào DB
        orderItemRepository.save(orderItem);
    }

    public List<OrderItem> getOrderItemsByOrderId(UUID orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }

    public OrderItem getOrderItemById(UUID id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy orderitem "));
    }


    public void deleteOrderItem(UUID id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tim thấy order item"));

        orderItemRepository.delete(orderItem);
    }
}
