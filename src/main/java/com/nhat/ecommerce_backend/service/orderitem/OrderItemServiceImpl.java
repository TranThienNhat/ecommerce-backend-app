package com.nhat.ecommerce_backend.service.orderitem;

import com.nhat.ecommerce_backend.entity.*;
import com.nhat.ecommerce_backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemRepository orderItemRepository;

    public void createOrderItem(Order savedOrder, List<CartItem> cartItems) {
        log.info("Creating order items for order id: {}", savedOrder.getId());

        List<OrderItem> orderItems = cartItems.stream()
                .map(ci -> {
                    log.debug("Creating order item for cart item id: {} with product id: {}", ci.getId(), ci.getProduct().getId());
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(ci.getProduct());
                    orderItem.setUnitPrice(ci.getProduct().getPrice());
                    orderItem.setQuantity(ci.getQuantity());
                    return orderItem;
                })
                .toList();

        orderItemRepository.saveAll(orderItems);

        log.info("Successfully created {} order items for order id: {}", orderItems.size(), savedOrder.getId());
    }
}
