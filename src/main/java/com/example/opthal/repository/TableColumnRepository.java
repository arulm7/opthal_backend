package com.example.opthal.repository;

import com.example.opthal.model.TableColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableColumnRepository extends JpaRepository<TableColumn, Long> {

    List<TableColumn> findByTableIdOrderByDisplayOrderAsc(Long tableId);
}