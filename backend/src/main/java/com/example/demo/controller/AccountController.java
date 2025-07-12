package com.example.demo.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.LogOutRequestDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.RefreshAccessTokenRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.RegisterResponseDto;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountController {

	@Autowired
	private AccountService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
		LoginResponseDto response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody LogOutRequestDto request) {
		authService.logout(request.getRefreshToken());
		return ResponseEntity.ok("Logout success");
	}

	@PostMapping("/refresh-access-token")
	public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshAccessTokenRequestDto request) {
		String refreshToken = request.getRefreshToken();
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new IllegalArgumentException("Refresh token is required");
		}
		String newAccessToken = authService.refreshAccessToken(refreshToken);
		return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto request) {
		RegisterResponseDto response = authService.register(request);
		return ResponseEntity.ok(response);
	}
//  @PostMapping("/forgot-password") 
//  @PostMapping("/reset-password") 
//  @PutMapping("/change-password") 

}
