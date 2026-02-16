package br.edu.iff.ccc.webdev.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SendChatMessageRequest(
        @NotNull 
        Long chatId,

        @NotBlank 
        @Size(max = 4000) 
        String content
) {}
