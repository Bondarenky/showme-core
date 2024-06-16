package com.itzroma.showme.domain.dto.response;

import java.util.List;

public record SubVideoResponseDto(String id, String name, List<SimpleVideoResponseDto> videos) {
}
