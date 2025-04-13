package com.nhat.ecommerce_backend.service.product;

import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    void updateProductFromDto(UpdateProductRequest dto, @MappingTarget Product product);
}
