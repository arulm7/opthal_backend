package com.example.opthal.repository;

import com.example.opthal.model.AnswerTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerTableRepository extends JpaRepository<AnswerTable, Long> {

    Optional<AnswerTable> findByAnswerBlockId(Long answerBlockId);
}