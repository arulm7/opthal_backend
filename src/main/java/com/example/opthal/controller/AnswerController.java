package com.example.opthal.controller;

import com.example.opthal.dto.AnswerResponse;
import com.example.opthal.dto.TableAnswerRequest;
import com.example.opthal.dto.TextAnswerRequest;
import com.example.opthal.model.AnswerBlock;
import com.example.opthal.service.AnswerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/{questionId}/answers/text")
    public AnswerBlock addTextAnswer(
            @PathVariable Long questionId,
            @RequestBody TextAnswerRequest request) {

        return answerService.addTextAnswer(questionId, request);
    }

    @PostMapping("/{questionId}/answers/table")
    public AnswerBlock addTableAnswer(
            @PathVariable Long questionId,
            @RequestBody TableAnswerRequest request) {

        return answerService.addTableAnswer(questionId, request);
    }

    @GetMapping("/{questionId}/answers")
    public List<AnswerResponse> getAnswers(
            @PathVariable Long questionId) {

        return answerService.getAnswers(questionId);
    }
}