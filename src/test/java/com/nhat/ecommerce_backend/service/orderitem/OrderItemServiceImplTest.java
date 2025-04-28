package com.nhat.ecommerce_backend.service.orderitem;

import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.entity.OrderItem;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.repository.OrderItemRepository;
import com.nhat.ecommerce_backend.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private ProductService productService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemServiceImpl;

    private Order mockSavedOrder;
    private List<CartItem> mockCartItems;
    private Product mockProduct;
    private CartItem mockCartItem;

    @BeforeEach
    void setUp() {

        mockSavedOrder = Order.builder()
                .id(UUID.randomUUID())
                .build();

        mockProduct = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .quantity(10)
                .build();

        mockCartItem = CartItem.builder()
                .id(UUID.randomUUID())
                .product(mockProduct)
                .quantity(2)
                .build();

        mockCartItems = List.of(mockCartItem);
    }

    @Test
    void createOrderItem_ShouldCreate_OrderItem() {
        Mockito.when(productService.getProductById(mockCartItem.getProduct().getId())).thenReturn(mockProduct);

        orderItemServiceImpl.createOrderItem(mockSavedOrder, mockCartItems);

        ArgumentCaptor<List<OrderItem>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(orderItemRepository, Mockito.times(1)).saveAll(captor.capture());

        List<OrderItem> saveOrderItem = captor.getValue();
        assertEquals(1, saveOrderItem.size());
    }

    @Test
    void getAllByOrderId_ShouldGetAll_OrderItem() {
        Mockito.when(orderItemRepository.findAllByOrderId(mockSavedOrder.getId())).thenReturn(List.of(new OrderItem()));

        List<OrderItem> result = orderItemServiceImpl.getAllByOrderId(mockSavedOrder.getId());

        Mockito.verify(orderItemRepository, Mockito.times(1)).findAllByOrderId(mockSavedOrder.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void createOrderItem_ShouldUpdateProductQuantity() {
        Mockito.when(productService.getProductById(mockProduct.getId()))
                .thenReturn(mockProduct);

        orderItemServiceImpl.createOrderItem(mockSavedOrder, mockCartItems);

        Mockito.verify(productService, Mockito.times(1))
                .updateProduct(Mockito.eq(mockProduct.getId()), Mockito.any(UpdateProductRequest.class));
    }
}