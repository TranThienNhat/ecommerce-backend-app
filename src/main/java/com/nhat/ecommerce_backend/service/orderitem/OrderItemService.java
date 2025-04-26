package com.nhat.ecommerce_backend.service.orderitem;

import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.entity.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {
    void createOrderItem(Order savedOrder, List<CartItem> cartItems);
    List<OrderItem> getAllByOrderId(UUID orderId);
}
