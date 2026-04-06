package br.edu.iff.ccc.webdev.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.Cookie;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Interceptor para autenticar conexões WebSocket usando JWT
 * Valida o token durante o handshake HTTP inicial
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = extractTokenFromCookie(servletRequest);

            if (token != null && !token.isBlank()) {
                try {
                    String username = jwtUtil.extractUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    if (jwtUtil.validateToken(token, userDetails)) {
                        attributes.put("username", username);
                        attributes.put("userDetails", userDetails);

                        log.info("WebSocket handshake successful for user: {}", username);
                        return true;
                    }
                } catch (Exception e) {
                    log.error("Error validating JWT token during WebSocket handshake", e);
                }
            }
            
            log.warn("WebSocket handshake failed: Invalid or missing token");
            return false;
        }

        return false;
    }

    private String extractTokenFromCookie(ServletServerHttpRequest servletRequest) {
        Cookie[] cookies = servletRequest.getServletRequest().getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        if (exception != null) {
            log.error("Error during WebSocket handshake", exception);
        }
    }
}
