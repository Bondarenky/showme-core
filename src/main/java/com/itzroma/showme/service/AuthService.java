package com.itzroma.showme.service;

import com.itzroma.showme.domain.dto.response.SignInResponseDto;
import com.itzroma.showme.domain.entity.User;

public interface AuthService {
    User signUpUser(User user);

    boolean verifyRegistration(String token);

    SignInResponseDto signInUser(String email, String password);
}
