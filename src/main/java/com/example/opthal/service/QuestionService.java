package com.example.opthal.service;

import com.example.opthal.model.AnswerBlock;
import com.example.opthal.model.AnswerBlockType;
import com.example.opthal.model.Question;
import com.example.opthal.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final FileStorageService fileStorageService;

    public QuestionService(
            QuestionRepository questionRepository,
            FileStorageService fileStorageService) {
        this.questionRepository = questionRepository;
        this.fileStorageService = fileStorageService;
    }

    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public Question updateQuestion(Long id, Question updatedQuestion) {

        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
        existingQuestion.setCategory(updatedQuestion.getCategory());

        return questionRepository.save(existingQuestion);
    }

    public String deleteQuestion(Long id) {

        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return "Question not found";
        }

        if (question.getAnswerBlocks() != null) {
            for (AnswerBlock block : question.getAnswerBlocks()) {
                if (block.getType() == AnswerBlockType.IMAGE && block.getContent() != null) {
                    fileStorageService.deleteAnswerImage(block.getContent());
                }
            }
        }

        questionRepository.delete(question);

        return "Question deleted successfully";
    }
}