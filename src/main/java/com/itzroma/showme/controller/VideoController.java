package com.itzroma.showme.controller;

import com.itzroma.showme.domain.dto.request.FindVideosRequestDto;
import com.itzroma.showme.domain.dto.response.SimpleVideoResponseDto;
import com.itzroma.showme.domain.dto.response.VideoResponseDto;
import com.itzroma.showme.domain.entity.HistoryEntry;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.domain.entity.Video;
import com.itzroma.showme.domain.entity.VideoType;
import com.itzroma.showme.exception.NotFoundException;
import com.itzroma.showme.exception.UnauthorizedException;
import com.itzroma.showme.service.HistoryEntryService;
import com.itzroma.showme.service.UserService;
import com.itzroma.showme.service.VideoService;
import com.itzroma.showme.service.VideoTypeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {
    private final UserService userService;
    private final VideoService videoService;
    private final VideoTypeService videoTypeService;
    private final HistoryEntryService historyEntryService;

    @Operation(summary = "Upload a video")
    @PostMapping
    public ResponseEntity<String> uploadVideo(@RequestParam("video") MultipartFile video,
                                              @RequestParam("preview") MultipartFile preview,
                                              @RequestParam("title") String title,
                                              @RequestParam("description") String description,
                                              @RequestParam("types") String videoTypes,
                                              Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new UnauthorizedException("Unauthorized")
        );
        List<VideoType> types;
        if (videoTypes == null || videoTypes.trim().isEmpty()) {
            types = Collections.emptyList();
        } else {
            types = Arrays.stream(videoTypes.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toLowerCase)
                    .map(StringUtils::capitalize)
                    .map(s -> videoTypeService.save(new VideoType(s)))
                    .toList();
        }
        Video uploadedVideo = videoService.uploadVideo(video, preview, user, title, description, types);
        return ResponseEntity.ok("Video uploaded: " + uploadedVideo.getTitle());
    }

    @Operation(summary = "Get video by id")
    @GetMapping("/one/{videoId}")
    public ResponseEntity<VideoResponseDto> getVideo(@PathVariable String videoId, Authentication authentication) {
        Video video = videoService.findVideoById(videoId).orElseThrow(
                () -> new NotFoundException("Video [%s] not found".formatted(videoId))
        );
        User user = authentication == null ? null : userService.findByEmail(authentication.getName()).orElseThrow(() -> new UnauthorizedException("Unauthorized"));
        if (user != null) {
            userService.updateHistory(user, video);
        }
        VideoResponseDto videoResponseDto = new VideoResponseDto(
                video.getId(),
                video.getVideoUrl(),
                video.getTitle(),
                video.getDescription(),
                video.getAuthor().getId(),
                video.getAuthor().getName(),
                video.getAuthor().getImageUrl(),
                video.getLikes().size(),
                user != null && video.getLikes().stream().map(User::getEmail).anyMatch(s -> s.equals(user.getEmail())),
                video.getDislikes().size(),
                user != null && video.getDislikes().stream().map(User::getEmail).anyMatch(s -> s.equals(user.getEmail())),
                user != null && user.getSubscriptions().contains(video.getAuthor())
        );
        return ResponseEntity.ok(videoResponseDto);
    }

    @Operation(summary = "Toggle like on the video by its id")
    @PostMapping("/{videoId}/toggle-like")
    public ResponseEntity<String> toggleLike(@PathVariable("videoId") String videoId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new UnauthorizedException("Unauthorized")
        );
        Video video = videoService.findVideoById(videoId).orElseThrow(
                () -> new NotFoundException("Video [%s] not found".formatted(videoId))
        );
        videoService.toggleLike(user, video);
        return ResponseEntity.ok("Like toggled on video: " + video.getTitle());
    }

    @Operation(summary = "Toggle dislike on the video by its id")
    @PostMapping("/{videoId}/toggle-dislike")
    public ResponseEntity<String> toggleDislike(@PathVariable("videoId") String videoId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new UnauthorizedException("Unauthorized")
        );
        Video video = videoService.findVideoById(videoId).orElseThrow(
                () -> new NotFoundException("Video [%s] not found".formatted(videoId))
        );
        videoService.toggleDislike(user, video);
        return ResponseEntity.ok("Dislike toggled on video: " + video.getTitle());
    }

    @PostMapping("/list")
    public ResponseEntity<List<SimpleVideoResponseDto>> find(@RequestBody FindVideosRequestDto dto) {
        VideoType videoType = videoTypeService.findByName(dto.type()).orElse(null);
        List<Video> videos = videoService.findBySearchTextAndTypes(dto.searchText(), videoType);
        List<SimpleVideoResponseDto> response = videos.stream()
                .map(video -> new SimpleVideoResponseDto(
                        video.getId(),
                        video.getPreviewUrl(),
                        video.getTitle(),
                        video.getAuthor().getId(),
                        video.getAuthor().getName(),
                        video.getAuthor().getImageUrl()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<SimpleVideoResponseDto>> getLikedVideos(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new UnauthorizedException("Unauthorized"));
        List<SimpleVideoResponseDto> likedVideos = user.getLikedVideos().stream()
                .map(video -> new SimpleVideoResponseDto(
                        video.getId(),
                        video.getPreviewUrl(),
                        video.getTitle(),
                        video.getAuthor().getId(),
                        video.getAuthor().getName(),
                        video.getAuthor().getImageUrl()
                ))
                .toList();
        return ResponseEntity.ok(likedVideos);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SimpleVideoResponseDto>> getHistory(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new UnauthorizedException("Unauthorized"));
        List<SimpleVideoResponseDto> history = historyEntryService.findByUserId(user.getId()).stream()
                .sorted(Comparator.comparing(HistoryEntry::getTimestamp).reversed())
                .map(entry -> {
                    Video video = videoService.findVideoById(entry.getVideoId()).orElseThrow(() -> new NotFoundException("Video [%s] not found".formatted(entry.getVideoId())));
                    return new SimpleVideoResponseDto(video.getId(), video.getPreviewUrl(), video.getTitle(), video.getAuthor().getId(), video.getAuthor().getName(), video.getAuthor().getImageUrl());
                })
                .toList();
        return ResponseEntity.ok(history);
    }
}
