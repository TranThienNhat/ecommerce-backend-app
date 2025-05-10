package com.nhat.ecommerce_backend.service.cartitem;

import com.nhat.ecommerce_backend.dto.cartItem.CartItemRequest;
import com.nhat.ecommerce_backend.dto.cartItem.CartItemResponse;
import com.nhat.ecommerce_backend.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.imageUrl", target = "productImage")
    @Mapping(source = "product.price", target = "price")
    @Mapping(target = "total", expression = "java(cartItem.getProduct().getPrice().multiply(new java.math.BigDecimal(cartItem.getQuantity())))")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateProductFromDto(CartItemRequest request, @MappingTarget CartItem cartItem);

    List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItems);
}
