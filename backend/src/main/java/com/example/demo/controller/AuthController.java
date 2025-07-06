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
import com.example.demo.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
//        return ResponseEntity.ok().build();

    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogOutRequestDto request) {
        String refreshToken = request.getRefreshToken();

        authService.logout(refreshToken);

        return ResponseEntity.ok("Logout success");
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
//        authManager.authenticate(new UsernamePasswordAuthenticationToken(
//            request.getUsername(), request.getPassword()));
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
//        String accessToken = jwtUtil.generateAccessToken(userDetails);
//        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
//
//        // Save refresh token to DB
//        RefreshToken tokenEntity = new RefreshToken();
//        tokenEntity.setToken(refreshToken);
//        tokenEntity.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
//        tokenEntity.setUser(userRepo.findByUsername(request.getUsername()).get());
//        tokenEntity.setRevoked(false);
//        refreshTokenRepo.save(tokenEntity);
//
//        // Set cookie
//        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
//            .httpOnly(true)
//            .secure(true)
//            .path("/api/auth/refresh-token")
//            .maxAge(7 * 24 * 60 * 60)
//            .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//
//        return ResponseEntity.ok(Map.of("accessToken", accessToken));
//    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
//        Cookie cookie = WebUtils.getCookie(request, "refreshToken");
//        if (cookie == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        String token = cookie.getValue();
//        if (!jwtUtil.validateToken(token, false)) return ResponseEntity.status(401).build();
//
//        String username = jwtUtil.extractUsername(token, false);
//        UserDetails user = userDetailsService.loadUserByUsername(username);
//        String newAccessToken = jwtUtil.generateAccessToken(user);
//
//        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//        Cookie cookie = WebUtils.getCookie(request, "refreshToken");
//        if (cookie != null) {
//            String token = cookie.getValue();
//            refreshTokenRepo.findByToken(token).ifPresent(t -> {
//                t.setRevoked(true);
//                refreshTokenRepo.save(t);
//            });
//
//            ResponseCookie cleared = ResponseCookie.from("refreshToken", "")
//                .httpOnly(true).secure(true).path("/api/auth/refresh-token")
//                .maxAge(0).build();
//            response.setHeader("Set-Cookie", cleared.toString());
//        }
//
//        return ResponseEntity.ok("Logged out");
//    }
}
