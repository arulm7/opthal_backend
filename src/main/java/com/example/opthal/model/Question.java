package com.example.opthal.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    private String category;

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<AnswerBlock> answerBlocks = new ArrayList<>();

    // Default constructor
    public Question() {
    }

    // Constructor
    public Question(Long id, String questionText, String category) {
        this.id = id;
        this.questionText = questionText;
        this.category = category;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<AnswerBlock> getAnswerBlocks() {
        return answerBlocks;
    }

    public void setAnswerBlocks(List<AnswerBlock> answerBlocks) {
        this.answerBlocks = answerBlocks;
    }
}