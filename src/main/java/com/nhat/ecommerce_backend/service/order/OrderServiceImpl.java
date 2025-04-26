package com.nhat.ecommerce_backend.service.order;

import com.nhat.ecommerce_backend.dto.order.OrdersRequest;
import com.nhat.ecommerce_backend.dto.order.UpdateOrderRequest;
import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.model.enums.Status;
import com.nhat.ecommerce_backend.repository.OrderRepository;
import com.nhat.ecommerce_backend.service.cartitem.CartItemService;
import com.nhat.ecommerce_backend.service.orderitem.OrderItemService;
import com.nhat.ecommerce_backend.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final UserService userService;
    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    @Transactional
    public void createOrder(OrdersRequest request) {

        log.info("Creating order for user with cart items: {}", request.getCartItemList());

        User user = userService.getProfile();
        List<CartItem> cartItems = cartItemService.getAllCartById(request.getCartItemList());

        if (cartItems.isEmpty()) {
            log.warn("No cart items found for user: {}", user.getId());
            throw new BusinessException("No cart items found");
        }

        BigDecimal total = cartItems.stream()
                .map(ci -> ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .totalPrice(total)
                .status(Status.PENDING)
                .build();

        var savedOrder = orderRepository.save(order);
        log.info("Order created successfully: {}", savedOrder.getId());

        orderItemService.createOrderItem(savedOrder,cartItems);

        cartItemService.deleteCartItems(request.getCartItemList());
    }

    public List<Order> getAllOrderByUserId() {
        User user = userService.getProfile();
        log.info("Fetching all orders for user: {}", user.getId());
        return orderRepository.findAllByUserId(user.getId());
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));
    }

    public void updateOrder(UUID orderId, UpdateOrderRequest request) {
        log.info("Updating order {} to status {}", orderId, request.getStatus());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", orderId);
                    return new BusinessException("Order not found");
                });

        order.setStatus(request.getStatus());
        orderRepository.save(order);

        log.info("Order {} updated successfully", orderId);
    }
}
