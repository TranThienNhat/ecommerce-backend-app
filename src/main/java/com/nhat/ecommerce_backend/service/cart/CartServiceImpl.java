package com.nhat.ecommerce_backend.service.cart;

import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    public void createCart(User user) {
        log.info("Creating new cart for userId={}", user.getId());
        Cart cart = new Cart();
        cart.setUser(user);

        cartRepository.save(cart);
        log.info("Successfully created cart with cartId={} for userId={}", cart.getId(), user.getId());
    }

    public Cart getCartById(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.warn("Cart not found for id: {}", cartId);
                    return new BusinessException("Cart not found");
                });
    }
}

