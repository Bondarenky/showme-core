package com.itzroma.showme.controller;

import com.itzroma.showme.domain.dto.response.SimpleVideoResponseDto;
import com.itzroma.showme.domain.dto.response.SubscriberResponseDto;
import com.itzroma.showme.domain.dto.response.UserProfileResponseDto;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.exception.NotFoundException;
import com.itzroma.showme.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Return user info by user id")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> profile(@PathVariable String userId) {
        User user = userService.findById(userId).orElseThrow(
                () -> new NotFoundException("User [%s] not found".formatted(userId))
        );
        List<SimpleVideoResponseDto> videos = user.getMyVideos().stream()
                .map(video -> new SimpleVideoResponseDto(
                        video.getId(),
                        video.getPreviewUrl(),
                        video.getTitle(),
                        video.getAuthor().getId(),
                        video.getAuthor().getName(),
                        video.getAuthor().getImageUrl()
                ))
                .toList();
        return ResponseEntity.ok(new UserProfileResponseDto(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), videos));
    }

    @Operation(summary = "Update user's profile picture")
    @PostMapping("/{userId}/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        String newImageUrl = userService.updateImage(userId, file);
        return ResponseEntity.ok(newImageUrl);
    }

    @PostMapping("/{userId}/toggle-sub")
    public ResponseEntity<String> toggleSub(@PathVariable String userId, Authentication authentication) {
        User sub = userService.findById(userId).orElseThrow(
                () -> new NotFoundException("User [%s] not found".formatted(userId))
        );
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User [%s] not found".formatted(userId))
        );
        String toggleResult = user.getSubscriptions().contains(sub)
                ? userService.unsubscribe(user, sub)
                : userService.subscribe(user, sub);
        return ResponseEntity.ok(toggleResult);
    }

    @GetMapping("/{userId}/subs")
    public ResponseEntity<List<SubscriberResponseDto>> getSubs(@PathVariable String userId) {
        List<SubscriberResponseDto> subs = userService.findById(userId).orElseThrow(
                        () -> new NotFoundException("User [%s] not found".formatted(userId))
                ).getSubscriptions().stream()
                .map(user -> new SubscriberResponseDto(user.getId(), user.getName(), user.getImageUrl()))
                .toList();
        return ResponseEntity.ok(subs);
    }
}
