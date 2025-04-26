package com.nhat.ecommerce_backend.service.cartitem;

import com.nhat.ecommerce_backend.dto.cartItem.CartItemRequest;
import com.nhat.ecommerce_backend.dto.cartItem.CartItemResponse;
import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CartItemRepository;
import com.nhat.ecommerce_backend.service.cart.CartService;
import com.nhat.ecommerce_backend.service.product.ProductService;
import com.nhat.ecommerce_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemServiceImpl implements CartItemService{

    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final UserService userService;

    public void createCartItem(CartItemRequest request) {
        var userId = userService.getProfile().getId();
        Product product = productService.getProductById(request.getProductId());
        Cart cart = cartService.getCartByUserId(userId);

        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        if (existing.isPresent()) {
            CartItem existingItem = existing.get();
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
            return;
        }

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                .build();

        cartItemRepository.save(cartItem);
    }

    public void updateCartItem(UUID cartItemId, CartItemRequest request) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException("CartItem Not Found For CartItemId"));

        cartItemMapper.updateProductFromDto(request, existingCartItem);
        cartItemRepository.save(existingCartItem);
    }

    public void deleteCartItems(List<UUID> cartItemsId) {
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemsId);
        if (cartItems.isEmpty()) {
            throw new BusinessException("No cartItems found to delete");
        }
        cartItemRepository.deleteAll(cartItems);
    }

    public List<CartItem> getAllCartById(List<UUID> cartItemList) {
        return cartItemRepository.findAllById(cartItemList);
    }

    public List<CartItemResponse> getCartItems() {
        var userId = userService.getProfile().getId();
        var cart = cartService.getCartByUserId(userId);
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());

        List<CartItemResponse> responses = cartItemMapper.toCartItemResponseList(cartItems);

        return responses;
    }
}
