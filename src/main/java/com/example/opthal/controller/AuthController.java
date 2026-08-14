package com.example.opthal.controller;

import com.example.opthal.dto.LoginRequest;
import com.example.opthal.dto.LoginResponse;
import com.example.opthal.dto.RegisterRequest;
import com.example.opthal.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.opthal.dto.ForgotPasswordRequest;
import com.example.opthal.dto.ResetPasswordRequest;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }
    @PostMapping("/login")
    private LoginResponse login(
            @RequestBody LoginRequest request
            ){
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String msg = authService.forgotPassword(request);
        return Map.of("message", msg);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String msg = authService.resetPassword(request);
            return ResponseEntity.ok(Map.of("message", msg));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}


