package com.example.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.Jwts;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authManager;
    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;


    public LoginResponseDto login(LoginRequestDto request) {
        // 1. Xác thực user
//    	loadUserByUsername được gọi ngầm 
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
            )
            
        );
        
        // 2. Lấy user từ DB
        User user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Tạo access token
        String accessToken = jwtUtil.generateAccessToken(user);

        // 4. Tạo refresh token và lưu DB
        String refreshToken = jwtUtil.generateRefreshToken(user);

        // 5. Trả về DTO
        return new LoginResponseDto(accessToken, refreshToken);
    }


}
