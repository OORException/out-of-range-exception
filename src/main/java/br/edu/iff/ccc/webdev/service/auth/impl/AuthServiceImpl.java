package br.edu.iff.ccc.webdev.service.auth.impl;

import br.edu.iff.ccc.webdev.dto.request.auth.LoginRequest;
import br.edu.iff.ccc.webdev.dto.request.auth.RegisterRequest;
import br.edu.iff.ccc.webdev.dto.response.auth.AuthResponse;
import br.edu.iff.ccc.webdev.exception.BadRequestException;
import br.edu.iff.ccc.webdev.exception.ConflictException;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.enums.UserLevel;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.security.JwtUtil;
import br.edu.iff.ccc.webdev.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username is already registered: " + request.username());
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email is already registered: " + request.email());
        }

        // Hash password with BCrypt
        String hashedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .username(request.username())
                .fullName(request.fullName())
                .email(request.email())
                .level(UserLevel.USER)
                .createdAt(Instant.now())
                .build();

        user.changePasswordHash(hashedPassword);

        user = userRepository.save(user);

        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                token
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        // Verify password with BCrypt
        if (!passwordEncoder.matches(request.password(), user.getPasswordHashForAuthentication())) {
            throw new BadRequestException("Invalid email or password");
        }

        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                token
        );
    }
}
