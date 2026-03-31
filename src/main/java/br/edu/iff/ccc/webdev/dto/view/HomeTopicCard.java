package br.edu.iff.ccc.webdev.dto.view;

public record HomeTopicCard(
        Long id,
        String title,
        String categoryName,
        long postCount,
        String lastActivityLabel
) {}
