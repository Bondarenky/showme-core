package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.domain.entity.Video;
import com.itzroma.showme.domain.entity.VideoType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface VideoService {
    Video uploadVideo(MultipartFile video, MultipartFile preview,
                      User author, String title, String description, List<VideoType> videoTypes);

    Optional<Video> findVideoById(String id);

    void toggleLike(User user, Video video);

    void toggleDislike(User user, Video video);

    List<Video> findBySearchTextAndTypes(String searchText, VideoType videoType);
}
