package com.example.opthal.dto;

public class TextAnswerRequest {

    private String content;
    private Integer displayOrder;

    public TextAnswerRequest() {
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
}