package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.dto.OrdersRequest;
import com.nhat.ecommerce_backend.dto.PlaceOrderRequest;
import com.nhat.ecommerce_backend.dto.UpdateOrderRequest;
import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.model.enums.Status;
import com.nhat.ecommerce_backend.repository.OrderRepository;
import com.nhat.ecommerce_backend.service.product.ProductServiceImpl;
import com.nhat.ecommerce_backend.service.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserServiceImpl userServiceImpl;
    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ProductServiceImpl productServiceImpl;

    @Transactional
    public Order createOrder(@Valid OrdersRequest request) {
        User user = userServiceImpl.findById(request.getUserId());
        List<CartItem> cartItems = cartItemService.findAllById(request.getCartItemList());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Không tìm thấy cart item nào");
        }

        BigDecimal total = cartItems.stream()
                .map(ci -> ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(total);
        order.setStatus(Status.PENDING);

        var savedOrder = orderRepository.save(order);

        orderItemService.createOrderItem(savedOrder,cartItems);
        cartItemService.deleteCartItems(request.getCartItemList());

        return savedOrder;
    }

    public List<Order> getAllOrderByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy order"));
    }

    public Order updateOrder(UUID orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy order"));

        order.setStatus(request.getStatus());
        return orderRepository.save(order);
    }

    @Transactional
    public Order createPlaceOrder(@Valid PlaceOrderRequest request) {
        // Tìm người dùng theo userId
        User user = userServiceImpl.findById(request.getUserId());

        // Tìm sản phẩm theo productId
        Product product = productServiceImpl.getProductById(request.getProductId());

        // Tính tổng giá trị đơn hàng
        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(total);
        order.setStatus(Status.PENDING);

        // Lưu đơn hàng
        Order savedPlaceOrder = orderRepository.save(order);

        // Tạo OrderItem cho đơn hàng
        orderItemService.createOrderItem(savedPlaceOrder, product, request.getQuantity());

        return savedPlaceOrder;
    }

}
