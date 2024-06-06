package com.itzroma.showme.domain.dto.request;

public record SignUpRequestDto(String name, String email, String password, String confirmPassword) {
}
