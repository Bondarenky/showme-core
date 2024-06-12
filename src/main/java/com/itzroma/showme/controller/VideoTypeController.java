package com.itzroma.showme.controller;

import com.itzroma.showme.domain.dto.response.VideoTypeResponseDto;
import com.itzroma.showme.service.VideoTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/types")
public class VideoTypeController {
    private final VideoTypeService videoTypeService;

    @GetMapping
    public ResponseEntity<List<VideoTypeResponseDto>> getAllTypes() {
        List<VideoTypeResponseDto> types = videoTypeService.findAll().stream()
                .map(videoType -> new VideoTypeResponseDto(videoType.getName()))
                .toList();
        return ResponseEntity.ok(types);
    }
}
