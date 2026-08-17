package com.example.opthal.controller;

import com.example.opthal.dto.ChangePasswordRequest;
import com.example.opthal.dto.UpdateProfileRequest;
import com.example.opthal.dto.UserProfileResponse;
import com.example.opthal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserProfileResponse getProfile(Authentication authentication) {
        // authentication.getName() returns the email used during login
        String email = authentication.getName();
        return userService.getProfile(email);
    }

    @PutMapping("/profile")
    public UserProfileResponse updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {
        
        String email = authentication.getName();
        return userService.updateProfile(email, request);
    }

    @PutMapping("/profile/password")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {

        String email = authentication.getName();
        try {
            userService.changePassword(email, request);
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
