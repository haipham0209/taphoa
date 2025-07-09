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
	    RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
	            .orElseThrow(() -> new IllegalArgumentException("Refresh token not valid"));

	    if (storedToken.isRevoked()) {
	        throw new IllegalStateException("Token is revoked");
	    }

	    if (storedToken.isExpired()) {
	        throw new IllegalStateException("Refresh token is expired");
	    }

	    User user = storedToken.getUser();
	    if (user == null) {
	        throw new IllegalStateException("User not found");
	    }

	    return jwtUtil.generateAccessToken(user);
	}


}
