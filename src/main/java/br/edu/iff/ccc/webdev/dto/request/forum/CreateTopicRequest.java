package br.edu.iff.ccc.webdev.dto.request.forum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateTopicRequest(
        @NotNull 
        Long categoryId,

        @NotBlank 
        @Size(max = 140) 
        String title,

        @Size(max = 2000) 
        String description,
        
        Set<Long> tagIds
) {}
