package br.edu.iff.ccc.webdev.dto.view;

public record PostDetailView(
        Long id,
        Long authorId,
        String authorUsername,
        String content,
        String createdAt,
        String updatedAt,
        long likeCount
) {}
