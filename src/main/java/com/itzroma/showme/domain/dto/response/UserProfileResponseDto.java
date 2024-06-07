package com.itzroma.showme.domain.dto.response;

import java.util.List;

public record UserProfileResponseDto(
        String userId,
        String userName,
        String userEmail,
        String userImageUrl,
        List<SimpleVideoResponseDto> videos
) {
}
