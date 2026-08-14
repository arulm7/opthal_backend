package com.example.opthal.service;

import com.example.opthal.model.Question;
import com.example.opthal.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
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

        if (!questionRepository.existsById(id)) {
            return "Question not found";
        }

        questionRepository.deleteById(id);

        return "Question deleted successfully";
    }
}