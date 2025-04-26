package com.nhat.ecommerce_backend.service.order;

import com.nhat.ecommerce_backend.dto.order.OrdersRequest;
import com.nhat.ecommerce_backend.dto.order.UpdateOrderRequest;
import com.nhat.ecommerce_backend.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    void createOrder(OrdersRequest request);
    List<Order> getAllOrderByUserId();
    Order getOrderById(UUID orderId);
    void updateOrder(UUID orderId, UpdateOrderRequest request);
}
