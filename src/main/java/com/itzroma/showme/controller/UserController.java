package com.itzroma.showme.controller;

import com.itzroma.showme.domain.dto.response.SimpleVideoResponseDto;
import com.itzroma.showme.domain.dto.response.UserProfileResponseDto;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.exception.NotFoundException;
import com.itzroma.showme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

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
                        video.getAuthor().getName()
                ))
                .toList();
        return ResponseEntity.ok(new UserProfileResponseDto(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), videos));
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        String newImageUrl = userService.updateImage(userId, file);
        return ResponseEntity.ok(newImageUrl);
    }
}
