package com.itzroma.showme.controller;

import com.itzroma.showme.domain.User;
import com.itzroma.showme.domain.dto.request.SignInRequestDto;
import com.itzroma.showme.domain.dto.request.SignUpRequestDto;
import com.itzroma.showme.domain.dto.response.SignInResponseDto;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto dto) {
        if (!Objects.equals(dto.password(), dto.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        User user = authService.signUp(new User(dto.name(), dto.email(), dto.password()));
        return ResponseEntity.ok("Sign up successful. Go to " + user.getEmail() + " to activate an account.");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token) {
        if (authService.verifyEmailVerificationToken(token)) {
            return ResponseEntity.ok("User is verified. You can close this tab.");
        }
        return new ResponseEntity<>("Invalid email verification link.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto dto) {
        String accessToken = authService.signIn(dto.email(), dto.password());
        return ResponseEntity.ok(new SignInResponseDto(accessToken));
    }
}
