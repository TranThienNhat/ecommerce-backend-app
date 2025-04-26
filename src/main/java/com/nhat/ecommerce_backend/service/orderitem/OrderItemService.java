package com.nhat.ecommerce_backend.service.orderitem;

import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Order;

import java.util.List;

public interface OrderItemService {
    void createOrderItem(Order savedOrder, List<CartItem> cartItems);
}
