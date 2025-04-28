package com.nhat.ecommerce_backend.service.auth;

import com.nhat.ecommerce_backend.config.JwtUtil;
import com.nhat.ecommerce_backend.dto.auth.LoginRequest;
import com.nhat.ecommerce_backend.dto.auth.LoginResponse;
import com.nhat.ecommerce_backend.service.refreshtoken.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {

        LoginRequest loginRequest = new LoginRequest("test@gmail.com", "12345678");

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginRequest.getEmail())).thenReturn(mockUserDetails);
        when(jwtUtil.generateAccessToken(mockUserDetails)).thenReturn("mocked-jwt-access-token");
        when(refreshTokenService.createRefreshToken(mockUserDetails)).thenReturn("mocked-jwt-refresh-token");

        LoginResponse response = authenticationService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getAccessToken());
        assertEquals("mocked-jwt-access-token", response.getRefreshToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
    }
}