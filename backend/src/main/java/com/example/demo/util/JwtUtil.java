package com.example.demo.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // Khóa bí mật để ký token – nên lưu ở nơi bảo mật
    private final SecretKey secretKey = Keys.hmacShaKeyFor("your-very-secure-secret-key-1234567890".getBytes());

    // Thời gian hết hạn token (1 giờ)
    private final long expirationMillis = 60 * 60 * 1000;

    // Tạo token từ username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Lấy username từ token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Kiểm tra token hết hạn chưa
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Validate token với UserDetails
    public boolean validateToken(String token, String expectedUsername) {
        String actualUsername = extractUsername(token);
        return (actualUsername.equals(expectedUsername) && !isTokenExpired(token));
    }

    // Lấy claims từ token
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
