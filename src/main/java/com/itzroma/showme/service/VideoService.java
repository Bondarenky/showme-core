package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.domain.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface VideoService {
    Video uploadVideo(MultipartFile video, MultipartFile preview, User author, String title, String description);

    Optional<Video> findVideoById(String id);

    void toggleLike(User user, Video video);

    void toggleDislike(User user, Video video);
}
