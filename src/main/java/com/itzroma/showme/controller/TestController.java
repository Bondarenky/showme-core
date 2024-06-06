package com.itzroma.showme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    @GetMapping("/all")
    public ResponseEntity<String> all() {
        return ResponseEntity.ok("hi all");
    }

    @GetMapping("/secured")
    public ResponseEntity<String> secured(Authentication auth) {
        return ResponseEntity.ok("hi " + auth.getName());
    }
}
