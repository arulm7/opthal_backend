package com.example.opthal.controller;

import com.example.opthal.dto.UpdateProfileRequest;
import com.example.opthal.dto.UserProfileResponse;
import com.example.opthal.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}
