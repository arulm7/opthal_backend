package com.example.opthal.controller;

import com.example.opthal.dto.UpdateProfileRequest;
import com.example.opthal.dto.UserProfileResponse;
import com.example.opthal.model.Role;
import com.example.opthal.service.UserService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private UserService userService;

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
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    public void testGetProfileSuccess() throws Exception {
        UserProfileResponse response = new UserProfileResponse(
                1L,
                "Test User",
                "test@gmail.com",
                "1234567890",
                Role.USER
        );

        Mockito.when(userService.getProfile("test@gmail.com"))
                .thenReturn(response);

        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.phone").value("1234567890"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    public void testGetProfileUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    public void testUpdateProfileSuccess() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest("Updated Name", "0987654321");
        UserProfileResponse response = new UserProfileResponse(
                1L,
                "Updated Name",
                "test@gmail.com",
                "0987654321",
                Role.USER
        );

        Mockito.when(userService.updateProfile(eq("test@gmail.com"), any(UpdateProfileRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.phone").value("0987654321"));
    }
}
