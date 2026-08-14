package com.example.opthal.repository;

import com.example.opthal.model.TableRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRowRepository extends JpaRepository<TableRow, Long> {

    List<TableRow> findByTableIdOrderByDisplayOrderAsc(Long tableId);
}