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

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<TableColumn> columns = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<TableRow> rows = new java.util.ArrayList<>();

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

    public java.util.List<TableColumn> getColumns() {
        return columns;
    }

    public void setColumns(java.util.List<TableColumn> columns) {
        this.columns = columns;
    }

    public java.util.List<TableRow> getRows() {
        return rows;
    }

    public void setRows(java.util.List<TableRow> rows) {
        this.rows = rows;
    }
}