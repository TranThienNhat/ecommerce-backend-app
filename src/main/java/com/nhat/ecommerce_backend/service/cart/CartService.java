package com.nhat.ecommerce_backend.service.cart;

import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.User;

import java.util.UUID;

public interface CartService {
    void createCart(User user);
    Cart getCartById(UUID cartId);
}
