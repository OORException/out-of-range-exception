package br.edu.iff.ccc.webdev.dto.request.forum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateTopicRequest(
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 140, message = "Title must not exceed 140 characters")
        String title,

        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description,

        Set<Long> tagIds
) {}
