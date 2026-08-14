package com.example.opthal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "answer_blocks")
public class AnswerBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnswerBlockType type;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer displayOrder;

    @JsonIgnore
    @OneToOne(mappedBy = "answerBlock", cascade = CascadeType.ALL, orphanRemoval = true)
    private AnswerTable answerTable;



    public AnswerBlock() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public AnswerBlockType getType() {
        return type;
    }

    public void setType(AnswerBlockType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public AnswerTable getAnswerTable() {
        return answerTable;
    }

    public void setAnswerTable(AnswerTable answerTable) {
        this.answerTable = answerTable;
    }
}