package com.example.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.entity.User.Status;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.Jwts;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	public LoginResponseDto login(LoginRequestDto request) {

		// Xác thực đăng nhập
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		// Nếu xác thực thành công, lấy user
		User user = userRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Tạo token
		String accessToken = jwtUtil.generateAccessToken(user);
		String refreshToken = jwtUtil.generateRefreshToken(user);

		return new LoginResponseDto(accessToken, refreshToken);
	}

	public void logout(String refreshTokenStr) {
		RefreshToken token = refreshTokenRepository.findByToken(refreshTokenStr)
	            .orElseThrow(() -> new IllegalArgumentException("Refresh token not exist"));
		
//	    if (token.isRevoked()) {
//	        throw new IllegalArgumentException("Refresh token đã bị thu hồi");
//	    }
	    
		token.setRevoked(true);
		refreshTokenRepository.save(token);
	}

	public String refreshAccessToken(String refreshToken) {
		// 1. Kiểm tra refresh token có trong DB
		RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
				.orElseThrow(() -> new RuntimeException("Refresh token not valid"));

		// 2. Kiểm tra token đã bị thu hồi chưa
		if (storedToken.isRevoked()) {
			throw new RuntimeException("token is revoked");
		}

		// 3. Kiểm tra token còn hạn không
		if (storedToken.isExpired()) {
//	        refreshTokenRepository.delete(storedToken); // tuỳ bạn muốn xoá hay giữ lại
			throw new RuntimeException("Refresh token is expired");
		}

		// 4. Lấy user từ token
		User user = storedToken.getUser(); // Dùng quan hệ @ManyToOne đã có

		if (user == null) {
			throw new RuntimeException("user not found");
		}

		// 5. Sinh access token mới
		String newAccessToken = jwtUtil.generateAccessToken(user);

		return newAccessToken;
	}

}
