package com.itzroma.showme.controller;

import com.itzroma.showme.domain.dto.response.VideoResponseDto;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.domain.entity.Video;
import com.itzroma.showme.exception.NotFoundException;
import com.itzroma.showme.exception.UnauthorizedException;
import com.itzroma.showme.service.UserService;
import com.itzroma.showme.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {
    private final UserService userService;
    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<String> uploadVideo(@RequestParam("video") MultipartFile video,
                                              @RequestParam("preview") MultipartFile preview,
                                              @RequestParam("title") String title,
                                              @RequestParam("description") String description,
                                              Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new UnauthorizedException("Unauthorized")
        );
        Video uploadedVideo = videoService.uploadVideo(video, preview, user, title, description);
        return ResponseEntity.ok("Video uploaded: " + uploadedVideo.getTitle());
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoResponseDto> getVideo(@PathVariable String videoId, Authentication authentication) {
        Video video = videoService.findVideoById(videoId).orElseThrow(
                () -> new NotFoundException("Video [%s] not found".formatted(videoId))
        );
        VideoResponseDto videoResponseDto = new VideoResponseDto(
                video.getId(),
                video.getVideoUrl(),
                video.getTitle(),
                video.getDescription(),
                video.getAuthor().getId(),
                video.getAuthor().getName(),
                video.getAuthor().getImageUrl(),
                video.getLikes().size(),
                video.getLikes().stream().map(User::getEmail).anyMatch(s -> s.equals(authentication.getName())),
                video.getDislikes().size(),
                video.getDislikes().stream().map(User::getEmail).anyMatch(s -> s.equals(authentication.getName()))
        );
        return ResponseEntity.ok(videoResponseDto);
    }

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
}
