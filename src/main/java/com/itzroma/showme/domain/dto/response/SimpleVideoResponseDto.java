package com.itzroma.showme.domain.dto.response;

public record SimpleVideoResponseDto(
        String videoId,
        String videoPreviewUrl,
        String videoTitle,
        String userId,
        String userName
) {
}
