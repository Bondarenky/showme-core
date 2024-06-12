package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.VideoType;
import com.itzroma.showme.repository.VideoTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class VideoTypeServiceImpl implements VideoTypeService {
    private final VideoTypeRepository videoTypeRepository;

    @Override
    public VideoType save(VideoType videoType) {
        AtomicReference<VideoType> toReturn = new AtomicReference<>();
        videoTypeRepository.findByName(videoType.getName()).ifPresentOrElse(
                toReturn::set,
                () -> toReturn.set(videoTypeRepository.save(videoType))
        );
        return toReturn.get();
    }

    @Override
    public List<VideoType> saveAll(VideoType... videoTypes) {
        return Arrays.stream(videoTypes).map(this::save).toList();
    }

    @Override
    public List<VideoType> findAll() {
        return videoTypeRepository.findAll();
    }
}
