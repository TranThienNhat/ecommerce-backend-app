package com.nhat.ecommerce_backend.service.user;

import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.model.enums.Role;
import com.nhat.ecommerce_backend.dto.user.RegisterRequest;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.repository.UserRepository;
import com.nhat.ecommerce_backend.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CartService cartService;

    public void registerUser(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed. Email already exists: {}", request.getEmail());
            throw new BusinessException("Email already exists !");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        log.info("User saved to database: {}", user.getEmail());

        cartService.createCart(user);
        log.info("Cart created for user: {}", user.getEmail());
    }

    public User getProfile() {
        log.info("Get authenticated user information");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("Unauthenticated user: {}", authentication != null ? authentication.getPrincipal() : "null");
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("Get user email information: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found"));

        return user;
    }

    public User findById(Long userId) {
        log.debug("Finding user with ID: {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new BusinessException("User not found");
                });
    }

    public User getByEmail(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BusinessException("User not found with email: " + username));
    }
}
