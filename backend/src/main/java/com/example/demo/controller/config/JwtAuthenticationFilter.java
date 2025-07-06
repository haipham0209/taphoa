package com.example.demo.controller.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Không có Authorization header hoặc không bắt đầu bằng "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7); // Cắt "Bearer "

        try {
            String email = jwtUtil.extractEmail(token);

            if (email != null && jwtUtil.validateToken(token, email)) {
                String role = jwtUtil.extractClaims(token).get("role", String.class);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("1");
            } else {
                // Nếu token không hợp lệ (expired, sai thông tin), trả về 401
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired or invalid");
                System.out.println("2");
                return;
            }
        } catch (Exception e) {
            // Trường hợp lỗi bất ngờ khi parse token
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            System.out.println("3");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
