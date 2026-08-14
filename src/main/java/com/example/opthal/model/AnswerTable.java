package com.example.opthal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "answer_tables")
public class AnswerTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "answer_block_id", nullable = false, unique = true)
    private AnswerBlock answerBlock;

    public AnswerTable() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnswerBlock getAnswerBlock() {
        return answerBlock;
    }

    public void setAnswerBlock(AnswerBlock answerBlock) {
        this.answerBlock = answerBlock;
    }
}