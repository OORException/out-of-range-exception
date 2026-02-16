package br.edu.iff.ccc.webdev.dto.request.forum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotNull 
        Long topicId,

        @NotBlank 
        @Size(max = 5000) 
        String content
) {}
