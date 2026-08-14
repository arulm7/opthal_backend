package com.example.opthal.repository;

import com.example.opthal.model.TableCell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableCellRepository extends JpaRepository<TableCell, Long> {

    List<TableCell> findByRowIdOrderByColumnOrderAsc(Long rowId);
}