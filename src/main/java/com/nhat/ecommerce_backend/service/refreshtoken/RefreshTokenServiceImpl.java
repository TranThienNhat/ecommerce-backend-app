package com.nhat.ecommerce_backend.service.refreshtoken;

import com.nhat.ecommerce_backend.config.JwtUtil;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenRequest;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenResponse;
import com.nhat.ecommerce_backend.entity.RefreshToken;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.UnauthorizedException;
import com.nhat.ecommerce_backend.repository.RefreshTokenRepository;
import com.nhat.ecommerce_backend.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public String createRefreshToken(UserDetails userDetails) {
        log.info("Creating refresh token for user: {}", userDetails.getUsername());
        User user = userService.getByEmail(userDetails.getUsername());

        if (user == null) {
            log.error("User not found with email: {}", userDetails.getUsername());
            throw new UsernameNotFoundException("User not found with email: " + userDetails.getUsername());
        }

        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        log.info("Generate refresh token for user: {}", userDetails.getUsername());

        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(user)
                .build();

        refreshTokenRepository.save(token);
        log.info("Save refresh token to database for user: {}", userDetails.getUsername());

        return token.getRefreshToken();
    }

    public RefreshTokenResponse createNewAccessToken(RefreshTokenRequest request) {
        log.info("Request to create new access token with refresh token: {}", request.getRefreshToken());

        String refreshToken = request.getRefreshToken();

        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            throw new UnauthorizedException("Refresh token expired or wrong signature");
        }

        RefreshToken stored = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (stored == null || !stored.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException("Refresh token is invalid or has been deleted");
        }


        String email = jwtUtil.extractEmailByRefreshToken(stored.getRefreshToken());
        log.info("Extracted email: {} from refresh token", email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        log.info("Generated new access token for user: {}", email);

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    @Transactional
    public void logout() {
        log.info("Logging out user: {}", userService.getProfile().getEmail());

        User user = userService.getProfile();
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
        if (refreshToken != null) {
            log.info("Deleting refresh token for user: {}", user.getEmail());
            refreshTokenRepository.delete(refreshToken);
        } else {
            log.warn("No refresh token found for user: {}", user.getEmail());
        }
    }
}
