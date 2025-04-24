package com.nhat.ecommerce_backend.service.cartitem;

import com.nhat.ecommerce_backend.dto.cartItem.CartItemRequest;
import com.nhat.ecommerce_backend.dto.cartItem.CartItemResponse;
import com.nhat.ecommerce_backend.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartItemMapper {
    void updateProductFromDto(CartItemRequest request, @MappingTarget CartItem cartItem);
    List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItems);
}
