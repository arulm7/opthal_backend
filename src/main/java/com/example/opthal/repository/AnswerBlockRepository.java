package com.example.opthal.repository;

import com.example.opthal.model.AnswerBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerBlockRepository extends JpaRepository<AnswerBlock, Long> {

    List<AnswerBlock> findByQuestionIdOrderByDisplayOrderAsc(Long questionId);
}