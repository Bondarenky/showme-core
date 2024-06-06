package com.itzroma.showme.service;

import com.itzroma.showme.domain.User;

public interface AuthService {
    User signUp(User user);

    boolean verifyEmailVerificationToken(String token);

    String signIn(String email, String password);
}
