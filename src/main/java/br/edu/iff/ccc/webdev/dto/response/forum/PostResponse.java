package br.edu.iff.ccc.webdev.dto.response.forum;

import java.time.Instant;

public record PostResponse(
        Long id,
        Long topicId,
        Long authorId,
        String content,
        Instant createdAt,
        Instant updatedAt
) {}
