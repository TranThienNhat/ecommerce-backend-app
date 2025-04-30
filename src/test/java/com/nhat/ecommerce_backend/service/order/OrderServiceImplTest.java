package com.nhat.ecommerce_backend.service.order;

import com.nhat.ecommerce_backend.dto.order.OrdersRequest;
import com.nhat.ecommerce_backend.dto.order.UpdateOrderRequest;
import com.nhat.ecommerce_backend.entity.*;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.model.enums.Status;
import com.nhat.ecommerce_backend.repository.OrderRepository;
import com.nhat.ecommerce_backend.service.cartitem.CartItemService;
import com.nhat.ecommerce_backend.service.mail.MailService;
import com.nhat.ecommerce_backend.service.orderitem.OrderItemService;
import com.nhat.ecommerce_backend.service.product.ProductService;
import com.nhat.ecommerce_backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private User mockUser;
    private Product mockProduct;
    private CartItem mockCartItem;
    private Order mockOrder;
    private OrdersRequest ordersRequest;
    private UpdateOrderRequest updateOrderRequest;
    private OrderItem mockOrderItem;
    private Product productBefore;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);

        mockProduct = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .build();

        productBefore = Product.builder() // <-- sửa ở đây
                .id(mockProduct.getId())
                .quantity(5)
                .build();

        mockCartItem = CartItem.builder()
                .id(UUID.randomUUID())
                .product(mockProduct)
                .quantity(1)
                .build();

        mockOrder = Order.builder()
                .id(UUID.randomUUID())
                .user(mockUser)
                .totalPrice(BigDecimal.valueOf(200))
                .status(Status.PENDING)
                .build();

        mockOrderItem = OrderItem.builder()
                .id(UUID.randomUUID())
                .order(mockOrder)
                .product(mockProduct)
                .quantity(2)
                .build();

        savedOrder = Order.builder()
                .id(UUID.randomUUID())
                .user(mockUser)
                .totalPrice(BigDecimal.TEN)
                .status(Status.PENDING)
                .build();

        ordersRequest = new OrdersRequest();
        ordersRequest.setCartItemList(List.of(mockCartItem.getId(), mockCartItem.getId()));

        updateOrderRequest = new UpdateOrderRequest();
        updateOrderRequest.setStatus(Status.CANCELLED);
    }

    @Test
    void createOrder_ShouldCreateOrder() {
        Mockito.when(userService.getProfile()).thenReturn(mockUser);
        Mockito.when(cartItemService.getAllCartById(ordersRequest.getCartItemList())).thenReturn(List.of(mockCartItem));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(savedOrder);

        orderServiceImpl.createOrder(ordersRequest);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(mailService).sendOrderCreatedEmail(Mockito.eq(mockUser), orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();
        assertEquals(savedOrder.getId(), capturedOrder.getId());

        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
        Mockito.verify(orderItemService, Mockito.times(1)).createOrderItem(Mockito.any(Order.class), Mockito.anyList());
        Mockito.verify(cartItemService, Mockito.times(1)).deleteCartItems(ordersRequest.getCartItemList());
    }

    @Test
    void createOrder_ShouldThrowException_WhenCartItemsEmpty() {
        Mockito.when(userService.getProfile()).thenReturn(mockUser);
        Mockito.when(cartItemService.getAllCartById(ordersRequest.getCartItemList())).thenReturn(Collections.emptyList());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderServiceImpl.createOrder(ordersRequest);
        });
        assertEquals("No cart items found", exception.getMessage());

        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any(Order.class));
    }

    @Test
    void getAllOrderByUserId_ShouldGetAll_Order() {
        Mockito.when(userService.getProfile()).thenReturn(mockUser);
        Mockito.when(orderRepository.findAllByUserId(mockUser.getId())).thenReturn(List.of(mockOrder));

        List<Order> result = orderServiceImpl.getAllOrderByUserId();

        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserId(mockUser.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockOrder.getId(), result.get(0).getId());
    }

    @Test
    void getOrderById_ShouldGetOrder_ById() {
        Mockito.when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.of(mockOrder));

        Order result = orderServiceImpl.getOrderById(mockOrder.getId());

        Mockito.verify(orderRepository, Mockito.times(1)).findById(mockOrder.getId());
        assertNotNull(result);
        assertEquals(mockOrder.getId(), result.getId());
        assertEquals(Status.PENDING, result.getStatus());
        assertEquals(BigDecimal.valueOf(200), result.getTotalPrice());
    }

    @Test
    void updateOrder_ShouldUpdate_Order() {
        Mockito.when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.of(mockOrder));
        Mockito.when(orderItemService.getAllByOrderId(mockOrder.getId())).thenReturn(List.of(mockOrderItem));
        Mockito.when(productService.getProductById(mockProduct.getId())).thenReturn(productBefore);
        updateOrderRequest.setStatus(Status.CANCELLED);

        orderServiceImpl.updateOrder(mockOrder.getId(), updateOrderRequest);

        Mockito.verify(orderItemService, Mockito.times(1)).getAllByOrderId(mockOrder.getId());
        Mockito.verify(productService, Mockito.times(1)).getProductById(mockProduct.getId());
        Mockito.verify(productService, Mockito.times(1))
                .updateProduct(Mockito.eq(mockProduct.getId()), Mockito.argThat(req -> req.getQuantity() == 7));
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));    }
}