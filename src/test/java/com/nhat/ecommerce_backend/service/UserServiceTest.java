package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.dto.user.request.RegisterRequest;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CartService cartService;

    @InjectMocks
    private UserService userService;


    @Test
    void registerUser_shouldThrowException_whenEmailExists() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> userService.registerUser(request));

        assertEquals("Email already exists !", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(cartService, never()).createCart(any());
    }

    @Test
    void registerUser_shouldSaveUserAndCreateCart_whenEmailNotExists() {

        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setPassword("Doe");
        request.setEmail("test@gmail.com");
        request.setPassword("12345678");
        request.setBirthDate(LocalDate.of(2000,1,1));
        request.setPhoneNumber("1234567890");
        request.setAddress("123 Son Tay");

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("12345678")).thenReturn("encodedPassword");

        userService.registerUser(request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(cartService).createCart(userArgumentCaptor.getValue());

        User saveUser = userArgumentCaptor.getValue();
        assertEquals("John", saveUser.getFirstName());
        assertEquals("encodedPassword", saveUser.getPassword());
    }
}