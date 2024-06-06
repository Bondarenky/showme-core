package com.itzroma.showme.service;

import com.itzroma.showme.domain.User;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.repository.UserRepository;
import com.itzroma.showme.util.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        validateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void enable(UUID id) {
        userRepository.enable(id.toString());
    }

    private void validateUser(User user) {
        validateRequiredFields(user);

        if (!FieldValidator.validateEmail(user.getEmail())) {
            throw new BadRequestException("Invalid email: " + user.getEmail());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already in use: " + user.getEmail());
        }

        if (!FieldValidator.validatePassword(user.getPassword(), false)) {
            throw new BadRequestException("Invalid password: " + user.getPassword());
        }
    }

    private void validateRequiredFields(User user) {
        if (FieldValidator.validateStringEmpty(user.getName())) {
            throw new BadRequestException("Name is required");
        }

        if (FieldValidator.validateStringEmpty(user.getEmail())) {
            throw new BadRequestException("Email is required");
        }

        if (FieldValidator.validateStringEmpty(user.getPassword())) {
            throw new BadRequestException("Password is required");
        }
    }
}
