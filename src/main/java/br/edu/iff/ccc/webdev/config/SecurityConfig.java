package br.edu.iff.ccc.webdev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for MVP
 * 
 * IMPORTANT: Security is DISABLED for MVP testing purposes.
 * This allows all requests without authentication.
 * 
 * TODO Phase 2:
 * - Enable authentication with JWT tokens
 * - Add BCrypt password hashing
 * - Implement role-based access control (USER vs ADMIN)
 * - Add CSRF protection
 * - Configure CORS properly
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
