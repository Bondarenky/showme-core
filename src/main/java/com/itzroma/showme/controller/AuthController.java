package com.itzroma.showme.controller;

import com.itzroma.showme.domain.dto.request.SignInRequestDto;
import com.itzroma.showme.domain.dto.request.SignUpRequestDto;
import com.itzroma.showme.domain.dto.response.SignInResponseDto;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.exception.BadRequestException;
import com.itzroma.showme.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Sign up user to the system and send an email verification request")
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto dto) {
        if (!Objects.equals(dto.password(), dto.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        User user = authService.signUpUser(new User(dto.name(), dto.email(), dto.password()));
        return ResponseEntity.ok("Sign up successful. Go to " + user.getEmail() + " to activate an account.");
    }

    @Operation(summary = "Verify registration by the token that was sent in email")
    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token) {
        if (authService.verifyRegistration(token)) {
            return ResponseEntity.ok("User is verified. You can close this tab.");
        }
        return new ResponseEntity<>("Invalid email verification link.", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Generate and return a JWT token that will be used to identify the user")
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto dto) {
        return ResponseEntity.ok(authService.signInUser(dto.email(), dto.password()));
    }
}
