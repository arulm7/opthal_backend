package com.example.opthal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "table_columns")
public class TableColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private AnswerTable table;

    @Column(nullable = false)
    private String columnName;

    @Column(nullable = false)
    private Integer displayOrder;

    public TableColumn() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnswerTable getTable() {
        return table;
    }

    public void setTable(AnswerTable table) {
        this.table = table;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}