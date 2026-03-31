package br.edu.iff.ccc.webdev.dto.view;

public record HomeChatCard(
        Long id,
        String name,
        String topicTitle,
        long participantCount,
        Long topicId
) {}
