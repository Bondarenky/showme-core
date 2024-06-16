package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.domain.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    void enable(UUID id);

    String subscribe(User user, User... subscribeTo);

    String unsubscribe(User user, User... unsubscribeFrom);

    void updateHistory(User user, Video video);

    void updateUser(String userId, MultipartFile image, String name);
}
