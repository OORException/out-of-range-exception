package br.edu.iff.ccc.webdev.dto.response.forum;

public record TagResponse(
        Long id,
        String name,
        long topicCount
) {}
