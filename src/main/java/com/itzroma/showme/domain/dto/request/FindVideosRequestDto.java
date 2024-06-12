package com.itzroma.showme.domain.dto.request;

import java.util.List;

public record FindVideosRequestDto(String searchText, List<String> types) {
}
