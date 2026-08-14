package com.example.opthal.dto;

import java.util.List;

public class TableResponse {

    private List<String> columns;
    private List<List<String>> rows;

    public TableResponse() {
    }

    public TableResponse(
            List<String> columns,
            List<List<String>> rows) {
        this.columns = columns;
        this.rows = rows;
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