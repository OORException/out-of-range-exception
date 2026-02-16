package br.edu.iff.ccc.webdev.service.auth.impl;

import br.edu.iff.ccc.webdev.dto.request.auth.LoginRequest;
import br.edu.iff.ccc.webdev.dto.request.auth.RegisterRequest;
import br.edu.iff.ccc.webdev.dto.response.auth.AuthResponse;
import br.edu.iff.ccc.webdev.exception.BadRequestException;
import br.edu.iff.ccc.webdev.exception.ConflictException;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.enums.UserLevel;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username is already registered: " + request.username());
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email is already registered: " + request.email());
        }

        // TODO: Add password hashing with BCrypt when security is enabled in Phase 2
        User user = User.builder()
                .username(request.username())
                .fullName(request.fullName())
                .email(request.email())
                .level(UserLevel.USER)
                .createdAt(Instant.now())
                .build();

        // Using internal method to set password hash
        user.changePasswordHash(request.password());

        user = userRepository.save(user);

        // TODO: Generate JWT token when security is enabled in Phase 2
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                "mock-token-" + user.getId()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        // TODO: Use BCrypt password verification when security is enabled in Phase 2
        String storedPassword = user.getPasswordHashForAuthentication();
        if (!storedPassword.equals(request.password())) {
            throw new BadRequestException("Invalid email or password");
        }

        // TODO: Generate JWT token when security is enabled in Phase 2
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                "mock-token-" + user.getId()
        );
    }
}
