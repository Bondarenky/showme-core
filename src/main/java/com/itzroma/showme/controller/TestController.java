package com.itzroma.showme.controller;

import com.itzroma.showme.domain.FileType;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.service.AmazonS3Service;
import com.itzroma.showme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final UserService userService;
    private final AmazonS3Service amazonS3Service;

    @GetMapping("/all")
    public ResponseEntity<String> all() {
        return ResponseEntity.ok("hi all");
    }

    @GetMapping("/secured")
    public ResponseEntity<String> secured(Authentication authentication) {
        return ResponseEntity.ok("hi " + authentication.getName());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam FileType fileType,
                                         Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new BadRequestException("User [%s] not found".formatted(authentication.getName()))
        );
        return ResponseEntity.ok(amazonS3Service.uploadFile(file, fileType, user.getId()));
    }
}
