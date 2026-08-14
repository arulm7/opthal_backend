package com.example.opthal.dto;

import com.example.opthal.model.AnswerBlockType;

public class AnswerResponse {

    private Long id;
    private AnswerBlockType type;
    private String content;
    private Integer displayOrder;
    private TableResponse table;

    public AnswerResponse() {
    }

    public AnswerResponse(
            Long id,
            AnswerBlockType type,
            String content,
            Integer displayOrder,
            TableResponse table) {

        this.id = id;
        this.type = type;
        this.content = content;
        this.displayOrder = displayOrder;
        this.table = table;
    }

    public Long getId() {
        return id;
    }

    public AnswerBlockType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public TableResponse getTable() {
        return table;
    }
}