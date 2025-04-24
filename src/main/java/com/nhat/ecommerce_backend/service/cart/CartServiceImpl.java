package com.nhat.ecommerce_backend.service.cart;

import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    public void createCart(User user) {

        Cart cart = new Cart();
        cart.setUser(user);

        cartRepository.save(cart);
    }

    public Cart getCartById(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(()-> new BusinessException("Cart not found"));
    }
}

