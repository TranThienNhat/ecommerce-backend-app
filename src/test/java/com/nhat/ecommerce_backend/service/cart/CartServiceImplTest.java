package com.nhat.ecommerce_backend.service.cart;

import com.nhat.ecommerce_backend.entity.Cart;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartServiceImp;

    private User user;
    private Cart cart;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUser(user);
    }

    @Test
    void createCart_ShouldCreate_Cart() {
        cartServiceImp.createCart(user);

        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        Mockito.verify(cartRepository, times(1)).save(captor.capture());

        Cart savedCart = captor.getValue();
        Assertions.assertNotNull(savedCart);
        Assertions.assertEquals(user.getId(), savedCart.getUser().getId());
    }

    @Test
    void getCartById_ShouldReturnCart_WhenFound() {
        UUID cartId = cart.getId();
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Cart result = cartServiceImp.getCartById(cartId);

        Assertions.assertEquals(cart, result);
    }

    @Test
    void getCartById_ShouldThrowException_WhenNotFound() {
        Mockito.when(cartRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID cartId = UUID.randomUUID();
        Assertions.assertThrows(BusinessException.class, () -> cartServiceImp.getCartById(cartId),"Cart Not found");
    }
}
