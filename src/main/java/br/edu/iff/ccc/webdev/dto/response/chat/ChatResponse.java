package br.edu.iff.ccc.webdev.dto.response.chat;

import java.time.Instant;

public record ChatResponse(
        Long id,
        Long topicId,
        String name,
        boolean active,
        Instant createdAt,
        long participantCount
) {}
