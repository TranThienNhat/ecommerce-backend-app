package com.nhat.ecommerce_backend.service.cartitem;

import com.nhat.ecommerce_backend.dto.cartItem.CartItemRequest;
import com.nhat.ecommerce_backend.dto.cartItem.CartItemResponse;
import com.nhat.ecommerce_backend.entity.CartItem;

import java.util.List;
import java.util.UUID;

public interface CartItemService {
    void createCartItem(CartItemRequest request);
    void updateCartItem(UUID cartItemId, CartItemRequest request);
    void deleteCartItems(List<UUID> cartItemsId);
    List<CartItem> findAllById(List<UUID> cartItemList);
    List<CartItemResponse> getCartItems();
}
