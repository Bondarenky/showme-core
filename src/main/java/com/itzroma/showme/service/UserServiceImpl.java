package com.itzroma.showme.service;

import com.itzroma.showme.domain.FileType;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.repository.UserRepository;
import com.itzroma.showme.util.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3Service amazonS3Service;

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

    @Override
    public String updateImage(String userId, MultipartFile file) {
        String imageUrl = amazonS3Service.uploadFile(file, FileType.AVATAR, userId);
        userRepository.updateImage(userId, imageUrl);
        return imageUrl;
    }

    private void validateUser(User user) {
        if (FieldValidator.validateStringEmpty(user.getName())) {
            throw new BadRequestException("Name is required");
        }
        if (FieldValidator.validateStringEmpty(user.getEmail())) {
            throw new BadRequestException("Email is required");
        }
        if (FieldValidator.validateStringEmpty(user.getPassword())) {
            throw new BadRequestException("Password is required");
        }
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
}
