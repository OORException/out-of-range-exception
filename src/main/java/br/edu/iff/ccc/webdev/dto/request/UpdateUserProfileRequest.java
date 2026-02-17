package br.edu.iff.ccc.webdev.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Size(min = 3, max = 120, message = "Full name must be between 3 and 120 characters")
        String fullName,

        @Email(message = "Email must be valid")
        @Size(max = 180, message = "Email must not exceed 180 characters")
        String email
) {}
