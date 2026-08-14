package com.example.opthal.controller;

import com.example.opthal.model.Question;
import com.example.opthal.service.QuestionService;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class QuestionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private QuestionService questionService;

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
    @WithMockUser(roles = "USER")
    public void testGetAllQuestionsAsUserSuccess() throws Exception {
        Question q1 = new Question(1L, "What is Glaucoma?", "Glaucoma");
        Question q2 = new Question(2L, "What is Cataract?", "Cataract");

        Mockito.when(questionService.getAllQuestions())
                .thenReturn(Arrays.asList(q1, q2));

        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText").value("What is Glaucoma?"))
                .andExpect(jsonPath("$[1].questionText").value("What is Cataract?"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateQuestionAsUserForbidden() throws Exception {
        Question q = new Question(null, "New Question?", "General");

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(q)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateQuestionAsAdminSuccess() throws Exception {
        Question q = new Question(null, "New Question?", "General");
        Question savedQ = new Question(10L, "New Question?", "General");

        Mockito.when(questionService.addQuestion(any(Question.class)))
                .thenReturn(savedQ);

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(q)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.questionText").value("New Question?"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateQuestionAsUserForbidden() throws Exception {
        Question q = new Question(1L, "Updated Question?", "General");

        mockMvc.perform(put("/api/questions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(q)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateQuestionAsAdminSuccess() throws Exception {
        Question q = new Question(1L, "Updated Question?", "General");
        Question updatedQ = new Question(1L, "Updated Question?", "General");

        Mockito.when(questionService.updateQuestion(eq(1L), any(Question.class)))
                .thenReturn(updatedQ);

        mockMvc.perform(put("/api/questions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(q)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.questionText").value("Updated Question?"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteQuestionAsUserForbidden() throws Exception {
        mockMvc.perform(delete("/api/questions/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteQuestionAsAdminSuccess() throws Exception {
        Mockito.when(questionService.deleteQuestion(1L))
                .thenReturn("Question deleted successfully");

        mockMvc.perform(delete("/api/questions/1"))
                .andExpect(status().isOk());
    }
}
