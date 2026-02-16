package br.edu.iff.ccc.webdev.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTagRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 60, message = "Name must not exceed 60 characters")
        String name
) {}
