package com.itzroma.showme.domain.dto.response;

public record CommentResponseDto(
        String commentId,
        String commentText,
        String userId,
        String userName,
        String userImageUrl
) {
}
