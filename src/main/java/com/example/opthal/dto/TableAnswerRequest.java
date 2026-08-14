package com.example.opthal.dto;

import java.util.List;

public class TableAnswerRequest {

    private Integer displayOrder;
    private List<String> columns;
    private List<List<String>> rows;

    public TableAnswerRequest() {
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }
}