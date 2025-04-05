package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.entity.*;
import com.nhat.ecommerce_backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
