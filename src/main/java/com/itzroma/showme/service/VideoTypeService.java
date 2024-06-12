package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.VideoType;

import java.util.Collection;
import java.util.List;

public interface VideoTypeService {
    VideoType save(VideoType videoType);

    List<VideoType> saveAll(VideoType... videoTypes);

    List<VideoType> findAll();

    List<VideoType> findAllByNames(Collection<String> names);
}
