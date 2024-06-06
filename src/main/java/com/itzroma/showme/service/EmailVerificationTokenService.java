package com.itzroma.showme.service;

import com.itzroma.showme.domain.EmailVerificationToken;
import com.itzroma.showme.domain.User;

import java.util.Optional;

public interface EmailVerificationTokenService {
    Optional<EmailVerificationToken> findByToken(String token);

    EmailVerificationToken generateEmailVerificationToken(User user);

    EmailVerificationToken validateEmailVerificationToken(String token);
}
