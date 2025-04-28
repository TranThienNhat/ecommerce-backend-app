package com.nhat.ecommerce_backend.service.refreshtoken;

import com.nhat.ecommerce_backend.config.JwtUtil;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenRequest;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenResponse;
import com.nhat.ecommerce_backend.entity.RefreshToken;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.RefreshTokenRepository;
import com.nhat.ecommerce_backend.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public String createRefreshToken(UserDetails userDetails) {
        User user = userService.getByEmail(userDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + userDetails.getUsername());
        }

        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(user)
                .build();

        refreshTokenRepository.save(token);
        return token.getRefreshToken();
    }

    public RefreshTokenResponse createNewAccessToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            throw new BusinessException("Invalid refresh token.");
        }

        RefreshToken storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        String email = jwtUtil.extractEmailByRefreshToken(storedRefreshToken.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    @Transactional
    public void logout() {
        User user = userService.getProfile();
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
        refreshTokenRepository.delete(refreshToken);
    }
}
