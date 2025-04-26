package com.nhat.ecommerce_backend.service.orderitem;

import com.nhat.ecommerce_backend.entity.*;
import com.nhat.ecommerce_backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemRepository orderItemRepository;

    public void createOrderItem(Order savedOrder, List<CartItem> cartItems) {
        List<OrderItem> orderItems = cartItems.stream()
                .map(ci -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(ci.getProduct());
                    orderItem.setUnitPrice(ci.getProduct().getPrice());
                    orderItem.setQuantity(ci.getQuantity());
                    return orderItem;
                })
                .toList();

        orderItemRepository.saveAll(orderItems);
    }
}
