package com.example.opthal.service;

import com.example.opthal.dto.UpdateProfileRequest;
import com.example.opthal.dto.UserProfileResponse;
import com.example.opthal.model.User;
import com.example.opthal.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToResponse(user);
    }

    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);

        return convertToResponse(updatedUser);
    }

    private UserProfileResponse convertToResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}
