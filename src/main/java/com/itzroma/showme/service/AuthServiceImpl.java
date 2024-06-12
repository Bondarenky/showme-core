package com.itzroma.showme.service;

import com.itzroma.showme.domain.dto.response.SignInResponseDto;
import com.itzroma.showme.domain.entity.VerificationToken;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.email.event.EmailVerificationEvent;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.security.JwtService;
import com.itzroma.showme.security.MyUserDetails;
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
public class AuthServiceImpl implements AuthService {
    private final VerificationTokenService verificationTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;

    @Value("${app.path}")
    private String appPath;

    @Override
    public User signUpUser(User user) {
        User savedUser = userService.save(user);
        VerificationToken verificationToken =
                verificationTokenService.generateVerificationToken(user);
        sendEmailVerificationToken(savedUser, verificationToken);
        return savedUser;
    }

    @Override
    public boolean verifyRegistration(String token) {
        VerificationToken verificationToken = verificationTokenService.validateVerificationToken(token);
        userService.enable(UUID.fromString(verificationToken.getUser().getId()));
        return true;
    }

    @Override
    public SignInResponseDto signInUser(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        validateLogin(password, userDetails);
        User user = userService.findByEmail(email).orElseThrow(() -> new BadRequestException("Bad Credentials"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password)
        );
        return new SignInResponseDto(user.getId(), jwtService.generateAccessToken((MyUserDetails) authentication.getPrincipal()));
    }

    private void sendEmailVerificationToken(User forUser, VerificationToken token) {
        publisher.publishEvent(new EmailVerificationEvent(forUser, appPath + "/auth", token));
    }

    private void validateLogin(String password, UserDetails userDetails) {
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadRequestException("Bad Credentials");
        }
        if (!userDetails.isEnabled()) {
            throw new BadRequestException("Your account is disabled");
        }
    }
}
