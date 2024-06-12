package com.itzroma.showme.controller;

import com.itzroma.showme.domain.entity.Comment;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.domain.entity.Video;
import com.itzroma.showme.exception.NotFoundException;
import com.itzroma.showme.exception.UnauthorizedException;
import com.itzroma.showme.service.CommentService;
import com.itzroma.showme.service.UserService;
import com.itzroma.showme.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final VideoService videoService;

    @Operation(summary = "Add comment to the video")
    @PostMapping("/{videoId}")
    public ResponseEntity<String> addComment(@PathVariable("videoId") String videoId,
                                             @RequestBody String text,
                                             Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new UnauthorizedException("Unauthorized")
        );
        Video video = videoService.findVideoById(videoId).orElseThrow(
                () -> new NotFoundException("Video [%s] not found")
        );
        commentService.addComment(new Comment(text, user, video));
        return ResponseEntity.ok("Comment added successfully");
    }
}
