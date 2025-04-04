package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.CartItemDeleteRequest;
import com.nhat.ecommerce_backend.dto.CartItemRequest;
import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<?> createCartItem(@RequestBody @Valid CartItemRequest request) {
        try {
            CartItem cartItem = cartItemService.createCartItem(request);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@RequestBody @Valid CartItemRequest request, @PathVariable UUID cartItemId) {
        try {
            CartItem cartItem = cartItemService.updatecartItem(cartItemId, request);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCartItems(@RequestBody @Valid CartItemDeleteRequest request) {
        cartItemService.deleteCartItems(request.getCartItemsId());
        return ResponseEntity.ok("Đã xóa thành công " + request.getCartItemsId().size() + " sản phẩm");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCartItemsByUserId(@PathVariable Long userId) {
        List<CartItem> cartItemList = cartItemService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItemList);
    }
}
