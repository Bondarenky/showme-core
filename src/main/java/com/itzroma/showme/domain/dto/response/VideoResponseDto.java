package com.itzroma.showme.domain.dto.response;

import java.util.List;

public record VideoResponseDto(
        String id,
        String title,
        String description,
        String authorId,
        String authorName,
        String authorImageUrl,
        int likes,
        int dislikes,
        List<CommentResponseDto> comments
) {
}
