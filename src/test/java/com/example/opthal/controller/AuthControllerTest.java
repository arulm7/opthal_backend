package com.example.opthal.controller;

import com.example.opthal.dto.LoginRequest;
import com.example.opthal.dto.LoginResponse;
import com.example.opthal.dto.RegisterRequest;
import com.example.opthal.dto.ForgotPasswordRequest;
import com.example.opthal.dto.ResetPasswordRequest;
import com.example.opthal.service.AuthService;
import com.example.opthal.security.JwtService;
import com.example.opthal.security.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@gmail.com");
        request.setPhone("1234567890");
        request.setPassword("password123");
        request.setConfirmPassword("password123");

        Mockito.when(authService.register(any(RegisterRequest.class)))
                .thenReturn("User registered successfully");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password123");

        LoginResponse response = new LoginResponse(
                "login success",
                "USER",
                "Test User",
                "mock-jwt-token"
        );

        Mockito.when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("login success"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    public void testForgotPassword() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest("test@gmail.com");

        Mockito.when(authService.forgotPassword(any(ForgotPasswordRequest.class)))
                .thenReturn("If the email is registered, a password reset link has been sent.");

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("If the email is registered, a password reset link has been sent."));
    }

    @Test
    public void testResetPasswordSuccess() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("some-token", "NewPassword@123");

        Mockito.when(authService.resetPassword(any(ResetPasswordRequest.class)))
                .thenReturn("Password reset successfully");

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset successfully"));
    }

    @Test
    public void testResetPasswordFailure() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("invalid-token", "NewPassword@123");

        Mockito.when(authService.resetPassword(any(ResetPasswordRequest.class)))
                .thenThrow(new RuntimeException("Invalid or expired reset token"));

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid or expired reset token"));
    }
}

