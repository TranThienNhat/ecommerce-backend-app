package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    public Optional<Cart> getCartForUser(Long userId) {
        return Optional.ofNullable(cartRepository.findByUserId(userId));
    }

    public void createCart(Long userId) {
        User user = userService.findById(userId);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());

        cartRepository.save(cart);
    }
}

