package br.edu.iff.ccc.webdev.dto.response;

import br.edu.iff.ccc.webdev.model.enums.UserLevel;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String fullName,
        String email,
        UserLevel level,
        Instant createdAt
) {}
