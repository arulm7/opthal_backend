package com.example.opthal.service;

import com.example.opthal.dto.LoginRequest;
import com.example.opthal.dto.LoginResponse;
import com.example.opthal.dto.RegisterRequest;
import com.example.opthal.model.Role;
import com.example.opthal.model.User;
import com.example.opthal.repository.UserRepository;
import com.example.opthal.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.opthal.dto.ForgotPasswordRequest;
import com.example.opthal.dto.ResetPasswordRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered";
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Every public registration is USER
        user.setRole(Role.USER);

        userRepository.save(user);

        return "User registered successfully";
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // Generate JWT after successful login
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(
                "login success",
                user.getRole().name(),
                user.getName(),
                token
        );
    }

    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
            userRepository.save(user);
            // Print to console so we can extract it in test scripts since email sending is mocked
            System.out.println("DEBUG RESET TOKEN for " + user.getEmail() + " is: " + token);
        }
        return "If the email is registered, a password reset link has been sent.";
    }

    public String resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        // Token matches and is not expired
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return "Password reset successfully";
    }
}