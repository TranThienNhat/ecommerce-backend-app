package com.nhat.ecommerce_backend.service.orderitem;

import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.*;
import com.nhat.ecommerce_backend.repository.OrderItemRepository;
import com.nhat.ecommerce_backend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    public void createOrderItem(Order savedOrder, List<CartItem> cartItems) {
        log.info("Creating order items for order id: {}", savedOrder.getId());

        List<OrderItem> orderItems = cartItems.stream()
                .map(ci -> {
                    log.debug("Creating order item for cart item id: {} with product id: {}", ci.getId(), ci.getProduct().getId());
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(ci.getProduct());
                    orderItem.setUnitPrice(ci.getProduct().getPrice());
                    orderItem.setQuantity(ci.getQuantity());

                    Product product = productService.getProductById(ci.getProduct().getId());
                    int newQuantity = product.getQuantity() - ci.getQuantity();
                    UpdateProductRequest productRequest = new UpdateProductRequest();
                    productRequest.setQuantity(newQuantity);

                    productService.updateProduct(product.getId(), productRequest);
                    log.info("Updating product id: {} quantity from {} to {}", product.getId(), product.getQuantity(), newQuantity);

                    return orderItem;
                })
                .toList();

        orderItemRepository.saveAll(orderItems);

        log.info("Successfully created {} order items for order id: {}", orderItems.size(), savedOrder.getId());
    }

    @Override
    public List<OrderItem> getAllByOrderId(UUID orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }
}
