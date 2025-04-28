package com.nhat.ecommerce_backend.service.refreshtoken;

import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenRequest;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {

    String createRefreshToken(UserDetails userDetails);
    RefreshTokenResponse createNewAccessToken(@Valid RefreshTokenRequest request);
    void logout();
}
