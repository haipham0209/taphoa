package com.example.demo.controller.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.io.IOException;
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
            throws ServletException, IOException, java.io.IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Nếu không có token thì bỏ qua, để filter tiếp theo xử lý
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            // Lấy email (username) từ token
            String email = jwtUtil.extractEmail(token);

            // Kiểm tra token còn hiệu lực và chưa bị thu hồi
            if (email != null && jwtUtil.validateToken(token, email)) {

                // Lấy role từ claim (nếu bạn lưu role trong token)
                String role = jwtUtil.extractClaims(token).get("role", String.class);

                // Tạo Authentication với role
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                );

                // Set Authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Token không hợp lệ hoặc hết hạn, có thể ghi log hoặc xử lý tùy ý
            // Không set Authentication -> truy cập sẽ bị chặn
        }

        filterChain.doFilter(request, response);
    }
}
