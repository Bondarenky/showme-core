package com.itzroma.showme.service;

import com.itzroma.showme.domain.EmailVerificationToken;
import com.itzroma.showme.domain.User;
import com.itzroma.showme.email.event.EmailVerificationEvent;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.security.DefaultUserDetails;
import com.itzroma.showme.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher publisher;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.path}")
    private String appPath;

    @Override
    public User signUp(User user) {
        User savedUser = userService.save(user);
        EmailVerificationToken emailVerificationToken =
                emailVerificationTokenService.generateEmailVerificationToken(user);
        sendEmailVerificationToken(savedUser, emailVerificationToken);
        return savedUser;
    }

    @Override
    public boolean verifyEmailVerificationToken(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.validateEmailVerificationToken(token);
        userService.enable(UUID.fromString(emailVerificationToken.getUser().getId()));
        return true;
    }

    @Override
    public String signIn(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadRequestException("Bad Credentials");
        }

        if (!userDetails.isEnabled()) {
            throw new BadRequestException("Your account is disabled");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password)
        );
        return jwtProvider.generateAccessToken((DefaultUserDetails) authentication.getPrincipal());
    }

    private void sendEmailVerificationToken(User forUser, EmailVerificationToken token) {
        String verificationLink = appPath + "/auth";
        publisher.publishEvent(new EmailVerificationEvent(forUser, verificationLink, token));
    }
}
