package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.cartItem.DeleteCartItemRequest;
import com.nhat.ecommerce_backend.dto.cartItem.CartItemRequest;
import com.nhat.ecommerce_backend.dto.cartItem.CartItemResponse;
import com.nhat.ecommerce_backend.service.cartitem.CartItemService;
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

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems() {
        List<CartItemResponse> responses = cartItemService.getCartItems();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<String> createCartItem(@RequestBody @Valid CartItemRequest request) {
        cartItemService.createCartItem(request);
        return ResponseEntity.ok("Create cartItem successfully");
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<String> updateCartItem(@RequestBody @Valid CartItemRequest request, @PathVariable UUID cartItemId) {
        cartItemService.updateCartItem(cartItemId, request);
        return ResponseEntity.ok("CartItem update successful");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCartItems(@RequestBody @Valid DeleteCartItemRequest request) {
        cartItemService.deleteCartItems(request.getCartItemsId());
        return ResponseEntity.ok("Deleted successfully" + request.getCartItemsId().size() + " product");
    }
}
