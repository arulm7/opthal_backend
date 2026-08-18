package com.example.opthal.controller;

import com.example.opthal.dto.AnswerResponse;
import com.example.opthal.dto.TableAnswerRequest;
import com.example.opthal.dto.TextAnswerRequest;
import com.example.opthal.model.AnswerBlock;
import com.example.opthal.service.AnswerService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/{questionId}/answers/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AnswerBlock addImageAnswer(
            @PathVariable Long questionId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "displayOrder", defaultValue = "1") Integer displayOrder) {

        return answerService.addImageAnswer(questionId, file, displayOrder);
    }

    @GetMapping("/answers/images/{filename:.+}")
    public ResponseEntity<Resource> viewAnswerImage(@PathVariable String filename) {
        Resource resource = answerService.getImageResource(filename);
        MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/{questionId}/answers/{answerId}/text")
    public AnswerBlock updateTextAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @RequestBody TextAnswerRequest request) {

        return answerService.updateTextAnswer(
                questionId,
                answerId,
                request
        );
    }

    @PutMapping("/{questionId}/answers/{answerId}/table")
    public AnswerBlock updateTableAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @RequestBody TableAnswerRequest request) {

        return answerService.updateTableAnswer(
                questionId,
                answerId,
                request
        );
    }

    @DeleteMapping("/{questionId}/answers/{answerId}")
    public String deleteAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId) {

        return answerService.deleteAnswer(
                questionId,
                answerId
        );
    }

    @GetMapping("/{questionId}/answers")
    public List<AnswerResponse> getAnswers(
            @PathVariable Long questionId) {

        return answerService.getAnswers(questionId);
    }
}