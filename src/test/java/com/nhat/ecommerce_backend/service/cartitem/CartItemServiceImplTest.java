package com.nhat.ecommerce_backend.service.cartitem;

import com.nhat.ecommerce_backend.dto.cartItem.CartItemRequest;
import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.CartItem;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CartItemRepository;
import com.nhat.ecommerce_backend.service.cart.CartService;
import com.nhat.ecommerce_backend.service.product.ProductService;
import com.nhat.ecommerce_backend.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

    @InjectMocks
    private CartItemServiceImpl cartItemServiceimpl;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    private User user;
    private Long productId;
    private UUID cartId;
    private UUID cartItemId;
    private Cart cart;
    private Product product;
    private CartItem cartItem;
    private CartItemRequest cartItemRequest;
    private List<UUID> cartItemsId;

    @BeforeEach
    void setUp() {
        user = new User();
        productId = 1L;
        cartId = UUID.randomUUID();
        cartItemId = UUID.randomUUID();
        cartItemsId = List.of(cartItemId);

        cart = Cart.builder()
                .id(cartId)
                .user(user)
                .build();

        product = Product.builder()
                .id(productId)
                .name("Sample Product")
                .build();

        cartItem = CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .product(product)
                .quantity(1)
                .build();

        cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(productId);
        cartItemRequest.setQuantity(2);
    }

    @Test
    void createCartItem_ShouldCreateCartItem() {
        Mockito.when(userService.getProfile()).thenReturn(user);
        Mockito.when(productService.getProductById(cartItemRequest.getProductId())).thenReturn(product);
        Mockito.when(cartService.getCartByUserId(user.getId())).thenReturn(cart);

        cartItemServiceimpl.createCartItem(cartItemRequest);

        Mockito.verify(cartItemRepository, Mockito.times(1)).save(Mockito.any(CartItem.class));
    }

    @Test
    void updateCartItem_ShouldUpdateCartItem() {
        Mockito.when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));
        Mockito.doNothing().when(cartItemMapper).updateProductFromDto(cartItemRequest, cartItem);

        cartItemServiceimpl.updateCartItem(cartItemId, cartItemRequest);

        Mockito.verify(cartItemMapper, Mockito.times(1)).updateProductFromDto(cartItemRequest, cartItem);
        Mockito.verify(cartItemRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void updateCartItem_ShouldThrowException() {
        Mockito.when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());


        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            cartItemServiceimpl.updateCartItem(cartItemId,cartItemRequest);
        });

        assertEquals("CartItem Not Found For CartItemId", exception.getMessage());
    }

    @Test
    void deleteCartItems_ShouldDeleteListCartItem() {
        Mockito.when(cartItemRepository.findAllById(cartItemsId)).thenReturn(List.of(cartItem));

        cartItemServiceimpl.deleteCartItems(cartItemsId);

        Mockito.verify(cartItemRepository, Mockito.times(1)).deleteAll(Mockito.anyList());
    }

    @Test
    void deleteCartItems_ShouldThrowException() {
        Mockito.when(cartItemRepository.findAllById(cartItemsId)).thenReturn(List.of());


        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartItemServiceimpl.deleteCartItems(cartItemsId);
        });

        assertEquals("No cartItems found to delete", exception.getMessage());
    }

    @Test
    void findAllById_GetCartItems() {
        Mockito.when(cartItemRepository.findAllById(cartItemsId)).thenReturn(List.of(cartItem));

        List<CartItem> result = cartItemServiceimpl.getAllCartById(cartItemsId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(List.of(cartItem), result);
        Mockito.verify(cartItemRepository, Mockito.times(1)).findAllById(cartItemsId);
    }
}