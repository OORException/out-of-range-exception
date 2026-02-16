package br.edu.iff.ccc.webdev.dto.response.chat;

import java.time.Instant;

public record ChatMessageResponse(
        Long id,
        Long chatId,
        Long senderId,
        String content,
        String status,
        Instant sentAt,
        Instant editedAt
) {}
