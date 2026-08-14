package com.example.opthal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "table_rows")
public class TableRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private AnswerTable table;

    @Column(nullable = false)
    private Integer displayOrder;

    public TableRow() {
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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}