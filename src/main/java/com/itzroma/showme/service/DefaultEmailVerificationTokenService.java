package com.itzroma.showme.service;

import com.itzroma.showme.domain.EmailVerificationToken;
import com.itzroma.showme.domain.User;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultEmailVerificationTokenService implements EmailVerificationTokenService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Override
    public Optional<EmailVerificationToken> findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token);
    }

    @Override
    public EmailVerificationToken generateEmailVerificationToken(User user) {
        return emailVerificationTokenRepository.save(
                new EmailVerificationToken(UUID.randomUUID().toString(), user)
        );
    }

    @Override
    @Transactional
    public EmailVerificationToken validateEmailVerificationToken(String token) {
        EmailVerificationToken emailVerificationToken = findByToken(token).orElseThrow(() -> {
            return new BadRequestException("Email verification token [%s] not found".formatted(token));
        });

        if (emailVerificationToken.getConfirmedAt() != null) {
            throw new BadRequestException("Email is already confirmed");
        }
        if (emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Email verification token is expired");
        }

        emailVerificationTokenRepository.confirm(token, LocalDateTime.now());
        return emailVerificationToken;
    }
}
