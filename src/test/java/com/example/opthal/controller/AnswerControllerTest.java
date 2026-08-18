package com.example.opthal.controller;

import com.example.opthal.dto.AnswerResponse;
import com.example.opthal.dto.TableAnswerRequest;
import com.example.opthal.dto.TextAnswerRequest;
import com.example.opthal.model.AnswerBlock;
import com.example.opthal.model.AnswerBlockType;
import com.example.opthal.service.AnswerService;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AnswerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private AnswerService answerService;

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
    public void testGetAnswersAsUserSuccess() throws Exception {
        AnswerResponse res = new AnswerResponse(
                1L,
                AnswerBlockType.TEXT,
                "Sample content",
                1,
                null
        );

        Mockito.when(answerService.getAnswers(1L))
                .thenReturn(Collections.singletonList(res));

        mockMvc.perform(get("/api/questions/1/answers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("Sample content"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAddTextAnswerAsUserForbidden() throws Exception {
        TextAnswerRequest request = new TextAnswerRequest();
        request.setContent("Some text");
        request.setDisplayOrder(1);

        mockMvc.perform(post("/api/questions/1/answers/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddTextAnswerAsAdminSuccess() throws Exception {
        TextAnswerRequest request = new TextAnswerRequest();
        request.setContent("Some text");
        request.setDisplayOrder(1);

        AnswerBlock block = new AnswerBlock();
        block.setId(2L);
        block.setContent("Some text");
        block.setType(AnswerBlockType.TEXT);
        block.setDisplayOrder(1);

        Mockito.when(answerService.addTextAnswer(eq(1L), any(TextAnswerRequest.class)))
                .thenReturn(block);

        mockMvc.perform(post("/api/questions/1/answers/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.content").value("Some text"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAddTableAnswerAsUserForbidden() throws Exception {
        TableAnswerRequest request = new TableAnswerRequest();
        request.setDisplayOrder(2);
        request.setColumns(Arrays.asList("Col1", "Col2"));
        request.setRows(Collections.singletonList(Arrays.asList("Val1", "Val2")));

        mockMvc.perform(post("/api/questions/1/answers/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddTableAnswerAsAdminSuccess() throws Exception {
        TableAnswerRequest request = new TableAnswerRequest();
        request.setDisplayOrder(2);
        request.setColumns(Arrays.asList("Col1", "Col2"));
        request.setRows(Collections.singletonList(Arrays.asList("Val1", "Val2")));

        AnswerBlock block = new AnswerBlock();
        block.setId(3L);
        block.setType(AnswerBlockType.TABLE);
        block.setDisplayOrder(2);

        Mockito.when(answerService.addTableAnswer(eq(1L), any(TableAnswerRequest.class)))
                .thenReturn(block);

        mockMvc.perform(post("/api/questions/1/answers/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.type").value("TABLE"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateTextAnswerAsUserForbidden() throws Exception {
        TextAnswerRequest request = new TextAnswerRequest();
        request.setContent("Updated text");
        request.setDisplayOrder(1);

        mockMvc.perform(put("/api/questions/1/answers/2/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateTextAnswerAsAdminSuccess() throws Exception {
        TextAnswerRequest request = new TextAnswerRequest();
        request.setContent("Updated text");
        request.setDisplayOrder(1);

        AnswerBlock block = new AnswerBlock();
        block.setId(2L);
        block.setContent("Updated text");
        block.setType(AnswerBlockType.TEXT);
        block.setDisplayOrder(1);

        Mockito.when(answerService.updateTextAnswer(eq(1L), eq(2L), any(TextAnswerRequest.class)))
                .thenReturn(block);

        mockMvc.perform(put("/api/questions/1/answers/2/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.content").value("Updated text"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteAnswerAsUserForbidden() throws Exception {
        mockMvc.perform(delete("/api/questions/1/answers/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteAnswerAsAdminSuccess() throws Exception {
        Mockito.when(answerService.deleteAnswer(1L, 2L))
                .thenReturn("Answer deleted successfully");

        mockMvc.perform(delete("/api/questions/1/answers/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Answer deleted successfully"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAddImageAnswerAsUserForbidden() throws Exception {
        org.springframework.mock.web.MockMultipartFile mockFile =
                new org.springframework.mock.web.MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy-bytes".getBytes());

        mockMvc.perform(multipart("/api/questions/1/answers/image")
                        .file(mockFile)
                        .param("displayOrder", "3"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddImageAnswerAsAdminSuccess() throws Exception {
        org.springframework.mock.web.MockMultipartFile mockFile =
                new org.springframework.mock.web.MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy-bytes".getBytes());

        AnswerBlock block = new AnswerBlock();
        block.setId(15L);
        block.setType(AnswerBlockType.IMAGE);
        block.setContent("550e8400-e29b-41d4-a716-446655440000.jpg");
        block.setDisplayOrder(3);

        Mockito.when(answerService.addImageAnswer(eq(1L), any(), eq(3)))
                .thenReturn(block);

        mockMvc.perform(multipart("/api/questions/1/answers/image")
                        .file(mockFile)
                        .param("displayOrder", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.type").value("IMAGE"))
                .andExpect(jsonPath("$.content").value("550e8400-e29b-41d4-a716-446655440000.jpg"))
                .andExpect(jsonPath("$.displayOrder").value(3));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testViewAnswerImageAsUserSuccess() throws Exception {
        org.springframework.core.io.ByteArrayResource resource =
                new org.springframework.core.io.ByteArrayResource("dummy-image-content".getBytes()) {
                    @Override
                    public String getFilename() {
                        return "550e8400-e29b-41d4-a716-446655440000.jpg";
                    }
                };

        Mockito.when(answerService.getImageResource("550e8400-e29b-41d4-a716-446655440000.jpg"))
                .thenReturn(resource);

        mockMvc.perform(get("/api/questions/answers/images/550e8400-e29b-41d4-a716-446655440000.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().bytes("dummy-image-content".getBytes()));
    }
}
