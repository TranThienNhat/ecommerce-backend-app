package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;


}
