package br.edu.iff.ccc.webdev.dto.request.forum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePostRequest(
        @NotBlank(message = "Content cannot be blank")
        @Size(max = 5000, message = "Content must not exceed 5000 characters")
        String content
) {}
