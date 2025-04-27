package com.nhat.ecommerce_backend.service.orderitem;

import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.entity.OrderItem;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemServiceImpl;

    private Order mockSavedOrder;
    private List<CartItem> mockCartItems;

    @BeforeEach
    void setUp() {

        mockSavedOrder = Order.builder()
                .id(UUID.randomUUID())
                .build();

        Product product = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .build();

        CartItem cartItem = CartItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(2)
                .build();

        mockCartItems = List.of(cartItem);
    }

    @Test
    void createOrderItem_ShouldCreate_OrderItem() {
        orderItemServiceImpl.createOrderItem(mockSavedOrder, mockCartItems);

        Mockito.verify(orderItemRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @Test
    void getAllByOrderId_ShouldGetAll_OrderItem() {
        Mockito.when(orderItemRepository.findAllByOrderId(mockSavedOrder.getId())).thenReturn(List.of(new OrderItem()));

        List<OrderItem> result = orderItemServiceImpl.getAllByOrderId(mockSavedOrder.getId());

        Mockito.verify(orderItemRepository, Mockito.times(1)).findAllByOrderId(mockSavedOrder.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}