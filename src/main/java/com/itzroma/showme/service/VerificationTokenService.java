package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.VerificationToken;
import com.itzroma.showme.domain.entity.User;

import java.util.Optional;

public interface VerificationTokenService {
    Optional<VerificationToken> findByToken(String token);

    VerificationToken generateVerificationToken(User user);

    VerificationToken validateVerificationToken(String token);
}
