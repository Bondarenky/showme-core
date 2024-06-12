package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.VerificationToken;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken generateVerificationToken(User user) {
        return verificationTokenRepository.save(
                new VerificationToken(UUID.randomUUID().toString(), user)
        );
    }

    @Override
    @Transactional
    public VerificationToken validateVerificationToken(String token) {
        VerificationToken verificationToken = findByToken(token).orElseThrow(() ->
                new BadRequestException("Verification token not found"));
        validateToken(verificationToken);
        verificationTokenRepository.confirm(token, LocalDateTime.now());
        return verificationToken;
    }

    private static void validateToken(VerificationToken verificationToken) {
        if (verificationToken.getConfirmedAt() != null) {
            throw new BadRequestException("Email is already confirmed");
        }
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification token is expired");
        }
    }
}
