package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.dto.CartItemRequest;
import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.repository.CartItemRepository;
import com.nhat.ecommerce_backend.service.product.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceImpl productServiceImpl;
    private final CartService cartService;

    public CartItem mapForCartItem(CartItemRequest request, CartItem existingCartItem) {
        UUID cartItemId = UUID.fromString(request.getCartId());
        Cart cart = cartService.getCartById(cartItemId);
        Product product = productServiceImpl.getProductById(request.getProductId());

        if (existingCartItem == null) {
            existingCartItem = new CartItem();
        }
        existingCartItem.setCart(cart);
        existingCartItem.setProduct(product);
        existingCartItem.setQuantity(request.getQuantity());

        return existingCartItem;
    }

    public CartItem createCartItem(CartItemRequest request) {
        var cartItem = mapForCartItem(request, null);
        return cartItemRepository.save(cartItem);
    }

    public CartItem updatecartItem(UUID cartItemId, CartItemRequest request) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cartitem !"));

        var cartItem = mapForCartItem(request, existingCartItem);
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItems(List<UUID> cartItemsId) {
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemsId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Không tìm thấy cartitem nào để xóa !");
        }
        cartItemRepository.deleteAll(cartItems);
    }

    public List<CartItem> findAllById(List<UUID> cartItemList) {
        return cartItemRepository.findAllById(cartItemList);
    }

    public List<CartItem> getCartItemByCart(UUID cartId) {
        return cartItemRepository.findAllByCartId(cartId);
    }
}
