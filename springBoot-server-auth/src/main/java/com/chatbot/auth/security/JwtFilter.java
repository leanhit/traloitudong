package com.chatbot.auth.security;

import com.chatbot.auth.service.AuthService;
import com.chatbot.auth.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public JwtFilter(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.objectMapper = new ObjectMapper(); // Khởi tạo ObjectMapper để chuyển đối tượng sang JSON
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtService.getEmailFromToken(token);
            } catch (ExpiredJwtException e) {
                // Xử lý khi token hết hạn
                sendErrorResponse(response, "Token đã hết hạn.");
                return; // Ngăn không cho filter chain tiếp tục
            } catch (SignatureException | MalformedJwtException e) {
                // Xử lý khi chữ ký token không hợp lệ hoặc token bị lỗi
                sendErrorResponse(response, "Token không hợp lệ.");
                return; // Ngăn không cho filter chain tiếp tục
            } catch (Exception e) {
                // Xử lý các lỗi khác
                sendErrorResponse(response, "Lỗi khi xử lý token.");
                return; // Ngăn không cho filter chain tiếp tục
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = authService.loadUserByUsername(email);

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * Gửi phản hồi lỗi về client với mã HTTP 401 và thông báo JSON.
     * @param response Đối tượng HttpServletResponse
     * @param message Thông báo lỗi
     * @throws IOException
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Đặt mã lỗi HTTP 401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Đặt Content-Type là application/json
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", message);
        errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);

        String json = objectMapper.writeValueAsString(errorDetails);
        response.getWriter().write(json);
    }
}
