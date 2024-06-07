package com.itzroma.showme.service;

import com.itzroma.showme.domain.dto.response.SignInResponseDto;
import com.itzroma.showme.domain.entity.User;

public interface AuthService {
    User signUp(User user);

    boolean verifyEmailVerificationToken(String token);

    SignInResponseDto signIn(String email, String password);
}
