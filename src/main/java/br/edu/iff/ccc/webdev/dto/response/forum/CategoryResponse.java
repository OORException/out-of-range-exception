package br.edu.iff.ccc.webdev.dto.response.forum;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        long topicCount
) {}
