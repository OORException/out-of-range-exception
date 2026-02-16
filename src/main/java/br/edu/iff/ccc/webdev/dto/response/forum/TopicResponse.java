package br.edu.iff.ccc.webdev.dto.response.forum;

import java.time.Instant;

public record TopicResponse(
        Long id,
        String title,
        String description,
        Long categoryId,
        Long createdBy,
        Instant createdAt,
        Instant lastActivityAt,
        long viewCount
) {}
