package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.EmailVerificationToken;
import com.itzroma.showme.domain.entity.User;

import java.util.Optional;

public interface EmailVerificationTokenService {
    Optional<EmailVerificationToken> findByToken(String token);

    EmailVerificationToken generateEmailVerificationToken(User user);

    EmailVerificationToken validateEmailVerificationToken(String token);
}
