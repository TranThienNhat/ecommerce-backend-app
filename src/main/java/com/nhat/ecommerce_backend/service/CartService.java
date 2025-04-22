package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.dto.cart.CartItemResponse;
import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CartRepository;
import com.nhat.ecommerce_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;

    private final UserService userService;

    public void createCart(User user) {

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());

        cartRepository.save(cart);
    }

    public Cart getCartById(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(()-> new BusinessException("Cart not found"));
    }

    public List<CartItemResponse> getCartItems() {
        Long user = userService.getProfile().getId();
        Cart cart = cartRepository.findByUserId(user)
                .orElseThrow(() -> new BusinessException("Cart not found"));
        List<CartItem> cartItems = cartItemService.getCartItemByCart(cart.getId());

        return cartItems.stream().map(item -> {
            Product product = item.getProduct();

            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setId(item.getId());
            cartItemResponse.setProductId(item.getProduct().getId());
            cartItemResponse.setProductName(item.getProduct().getName());
            cartItemResponse.setProductImage(item.getProduct().getImageUrl());
            cartItemResponse.setPrice(product.getPrice());
            cartItemResponse.setQuantity(item.getQuantity());
            cartItemResponse.setTotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            return cartItemResponse;
        }).collect(Collectors.toList());
    }
}

