package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.VideoType;

import java.util.List;
import java.util.Optional;

public interface VideoTypeService {
    VideoType save(VideoType videoType);

    List<VideoType> saveAll(VideoType... videoTypes);

    List<VideoType> findAll();

    Optional<VideoType> findByName(String name);
}
