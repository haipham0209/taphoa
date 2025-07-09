package com.example.demo.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.LogOutRequestDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
	    LoginResponseDto response = authService.login(request);
	    return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody LogOutRequestDto request) {
	    authService.logout(request.getRefreshToken()); // nếu có lỗi, sẽ throw và được handler xử lý
	    return ResponseEntity.ok("Logout success");
	}


	@PostMapping("/refresh-access-token")
	public ResponseEntity<?> refreshAccessToken(@RequestBody LogOutRequestDto request) {
		String refreshToken = request.getRefreshToken();
		if (refreshToken == null || refreshToken.isEmpty()) {
			return ResponseEntity.badRequest().body("Refresh token is requied");
		}

		try {
			String newAccessToken = authService.refreshAccessToken(refreshToken);
			return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
}
