package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	    try {
	        LoginResponseDto response = authService.login(request);
	        return ResponseEntity.ok(response);
	    } catch (UsernameNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	    } catch (BadCredentialsException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
	    }
	}


	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody LogOutRequestDto request) {
	    try {
	        authService.logout(request.getRefreshToken());
	        return ResponseEntity.ok("Logout success");
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    }
	}


	// Endpoint refresh access token
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
