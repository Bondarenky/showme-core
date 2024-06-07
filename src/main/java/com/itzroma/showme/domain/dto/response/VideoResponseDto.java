package com.itzroma.showme.domain.dto.response;

public record VideoResponseDto(
        String id,
        String videoUrl,
        String title,
        String description,
        String authorId,
        String authorName,
        String authorImageUrl,
        int likes,
        int dislikes
) {
}
