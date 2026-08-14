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

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<TableCell> cells = new java.util.ArrayList<>();

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

    public java.util.List<TableCell> getCells() {
        return cells;
    }

    public void setCells(java.util.List<TableCell> cells) {
        this.cells = cells;
    }
}