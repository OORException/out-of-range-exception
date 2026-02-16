package br.edu.iff.ccc.webdev.dto.request.chat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateChatRequest(
        @NotNull(message = "Topic ID cannot be null")
        Long topicId,

        @Size(max = 120, message = "Chat name must not exceed 120 characters")
        String name
) {}
