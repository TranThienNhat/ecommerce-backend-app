package com.nhat.ecommerce_backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String ACCESS_SECRET_KEY = "anhyeuemnhieunhieunhieunhieulam123456";
    private static final String REFRESH_SECRET_KEY = "emyeuanhnhieunhieunhieunhieulam654321";

    private final Key refreshKey = Keys.hmacShaKeyFor(REFRESH_SECRET_KEY.getBytes());
    private final Key accessKey = Keys.hmacShaKeyFor(ACCESS_SECRET_KEY.getBytes());

    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role",userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 ngày
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractEmailByAccessToken(String token) {return parseAccessToken(token).getBody().getSubject();}

    public boolean isTokenValid(String token) {
        try {
            parseAccessToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractEmailByRefreshToken(String token) {return parseRefreshToken(token).getBody().getSubject();}


    private Jws<Claims> parseAccessToken (String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token);
    }

    private Jws<Claims> parseRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token);
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            parseRefreshToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
