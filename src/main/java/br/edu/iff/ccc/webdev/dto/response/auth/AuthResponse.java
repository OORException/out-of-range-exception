package br.edu.iff.ccc.webdev.dto.response.auth;

public record AuthResponse(
        Long userId,
        String username,
        String email,
        String fullName,
        String token
) {}
