package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    void enable(UUID id);

    String updateImage(String userId, MultipartFile file);
}
