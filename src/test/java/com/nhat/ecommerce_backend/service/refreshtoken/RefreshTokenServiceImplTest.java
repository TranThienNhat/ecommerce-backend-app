package com.nhat.ecommerce_backend.service.refreshtoken;

import com.nhat.ecommerce_backend.config.JwtUtil;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenRequest;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenResponse;
import com.nhat.ecommerce_backend.entity.RefreshToken;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.exception.UnauthorizedException;
import com.nhat.ecommerce_backend.repository.RefreshTokenRepository;
import com.nhat.ecommerce_backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private User mockUser;
    private UserDetails userDetails;
    private RefreshTokenRequest request;
    private RefreshToken mockStoredToken;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);

        userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@test.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        mockStoredToken = new RefreshToken();
        mockStoredToken.setRefreshToken("mock-refresh-token");

        request = new RefreshTokenRequest();
        request.setRefreshToken("mock-refresh-token");
    }


    @Test
    void createRefreshToken_ShouldCreate_Token() {

        Mockito.when(userService.getByEmail(userDetails.getUsername())).thenReturn(mockUser);
        Mockito.when(jwtUtil.generateRefreshToken(userDetails)).thenReturn("string-refresh-token");

        refreshTokenService.createRefreshToken(userDetails);

        Mockito.verify(refreshTokenRepository, Mockito.times(1)).save(Mockito.any(RefreshToken.class));
    }

    @Test
    void createRefreshToken_ShouldThrow_Exception() {
        Mockito.when(userService.getByEmail(userDetails.getUsername())).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            refreshTokenService.createRefreshToken(userDetails);
        });

        assertEquals("User not found with email: " + userDetails.getUsername(), exception.getMessage());
    }

    @Test
    void createNewAccessToken_ShouldCreate_NewAccess() {
        String mockRefreshToken = "mock-refresh-token";
        String mockEmail = "test@test.com";
        Mockito.when(jwtUtil.isRefreshTokenValid(request.getRefreshToken())).thenReturn(true);
        Mockito.when(refreshTokenRepository.findByRefreshToken(mockRefreshToken)).thenReturn(mockStoredToken);
        Mockito.when(jwtUtil.extractEmailByRefreshToken(mockRefreshToken)).thenReturn(mockEmail);
        Mockito.when(userDetailsService.loadUserByUsername(mockEmail)).thenReturn(userDetails);
        Mockito.when(jwtUtil.generateAccessToken(userDetails)).thenReturn("mock-access-token");

        RefreshTokenResponse response = refreshTokenService.createNewAccessToken(request);

        assertNotNull(response);
        assertEquals("mock-access-token", response.getAccessToken());
    }

    @Test
    void createNewAccessToken_ShouldThrow_Exception() {
        request.setRefreshToken("mock-refresh-token-new");
        Mockito.when(jwtUtil.isRefreshTokenValid(request.getRefreshToken())).thenReturn(false);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            refreshTokenService.createNewAccessToken(request);
        });

        assertNotNull(exception);
        assertEquals("Refresh token expired or wrong signature", exception.getMessage());
    }

    @Test
    void logout_ShouldLogout() {
        Mockito.when(userService.getProfile()).thenReturn(mockUser);
        Mockito.when(refreshTokenRepository.findByUserId(mockUser.getId())).thenReturn(mockStoredToken);

        refreshTokenService.logout();

        Mockito.verify(refreshTokenRepository, Mockito.times(1)).delete(mockStoredToken);
    }
}