package com.example.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;


@Service
public class AuthService {

    @Autowired private AuthenticationManager authManager;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private RefreshTokenRepository refreshRepo;

    public LoginResponseDto login(LoginRequestDto request) {
        // 1. Xác thực user
    	System.out.println("Debug");
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
            )
            
        );
        
        System.out.println("After authenticate");
        // 2. Lấy user từ DB
        User user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Tạo access token
        String accessToken = jwtUtil.generateToken(user.getUserName());

        // 4. Tạo refresh token và lưu DB
        String refreshToken = createRefreshToken(user);

        // 5. Trả về DTO
        return new LoginResponseDto(accessToken, refreshToken);
    }

    private String createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        token.setRevoked(false);
        refreshRepo.save(token);
        return token.getToken();
    }
}
