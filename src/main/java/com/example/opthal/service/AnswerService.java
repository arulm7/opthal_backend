package com.example.opthal.service;

import com.example.opthal.dto.AnswerResponse;
import com.example.opthal.dto.TableAnswerRequest;
import com.example.opthal.dto.TableResponse;
import com.example.opthal.dto.TextAnswerRequest;
import com.example.opthal.model.AnswerBlock;
import com.example.opthal.model.AnswerBlockType;
import com.example.opthal.model.AnswerTable;
import com.example.opthal.model.Question;
import com.example.opthal.model.TableCell;
import com.example.opthal.model.TableColumn;
import com.example.opthal.model.TableRow;
import com.example.opthal.repository.AnswerBlockRepository;
import com.example.opthal.repository.AnswerTableRepository;
import com.example.opthal.repository.QuestionRepository;
import com.example.opthal.repository.TableCellRepository;
import com.example.opthal.repository.TableColumnRepository;
import com.example.opthal.repository.TableRowRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerBlockRepository answerBlockRepository;
    private final QuestionRepository questionRepository;
    private final AnswerTableRepository answerTableRepository;
    private final TableColumnRepository tableColumnRepository;
    private final TableRowRepository tableRowRepository;
    private final TableCellRepository tableCellRepository;

    public AnswerService(
            AnswerBlockRepository answerBlockRepository,
            QuestionRepository questionRepository,
            AnswerTableRepository answerTableRepository,
            TableColumnRepository tableColumnRepository,
            TableRowRepository tableRowRepository,
            TableCellRepository tableCellRepository) {

        this.answerBlockRepository = answerBlockRepository;
        this.questionRepository = questionRepository;
        this.answerTableRepository = answerTableRepository;
        this.tableColumnRepository = tableColumnRepository;
        this.tableRowRepository = tableRowRepository;
        this.tableCellRepository = tableCellRepository;
    }


    // =========================
    // ADD TEXT ANSWER
    // =========================

    public AnswerBlock addTextAnswer(
            Long questionId,
            TextAnswerRequest request) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new RuntimeException("Question not found"));

        AnswerBlock answerBlock = new AnswerBlock();

        answerBlock.setQuestion(question);
        answerBlock.setType(AnswerBlockType.TEXT);
        answerBlock.setContent(request.getContent());
        answerBlock.setDisplayOrder(request.getDisplayOrder());

        return answerBlockRepository.save(answerBlock);
    }


    // =========================
    // ADD TABLE ANSWER
    // =========================

    public AnswerBlock addTableAnswer(
            Long questionId,
            TableAnswerRequest request) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new RuntimeException("Question not found"));

        AnswerBlock answerBlock = new AnswerBlock();

        answerBlock.setQuestion(question);
        answerBlock.setType(AnswerBlockType.TABLE);
        answerBlock.setDisplayOrder(request.getDisplayOrder());

        AnswerBlock savedAnswerBlock =
                answerBlockRepository.save(answerBlock);


        // Create AnswerTable
        AnswerTable answerTable = new AnswerTable();

        answerTable.setAnswerBlock(savedAnswerBlock);

        AnswerTable savedTable =
                answerTableRepository.save(answerTable);


        // Create columns
        List<String> columns = request.getColumns();

        for (int i = 0; i < columns.size(); i++) {

            TableColumn tableColumn = new TableColumn();

            tableColumn.setTable(savedTable);
            tableColumn.setColumnName(columns.get(i));
            tableColumn.setDisplayOrder(i + 1);

            tableColumnRepository.save(tableColumn);
        }


        // Create rows and cells
        List<List<String>> rows = request.getRows();

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {

            TableRow tableRow = new TableRow();

            tableRow.setTable(savedTable);
            tableRow.setDisplayOrder(rowIndex + 1);

            TableRow savedRow =
                    tableRowRepository.save(tableRow);


            List<String> cells = rows.get(rowIndex);

            for (int columnIndex = 0;
                 columnIndex < cells.size();
                 columnIndex++) {

                TableCell tableCell = new TableCell();

                tableCell.setRow(savedRow);
                tableCell.setContent(cells.get(columnIndex));
                tableCell.setColumnOrder(columnIndex + 1);

                tableCellRepository.save(tableCell);
            }
        }

        return savedAnswerBlock;
    }


    // =========================
    // UPDATE TEXT ANSWER
    // =========================

    public AnswerBlock updateTextAnswer(
            Long questionId,
            Long answerId,
            TextAnswerRequest request) {

        if (!questionRepository.existsById(questionId)) {
            throw new RuntimeException("Question not found");
        }

        AnswerBlock answerBlock = answerBlockRepository
                .findById(answerId)
                .orElseThrow(() ->
                        new RuntimeException("Answer not found"));

        if (!answerBlock.getQuestion().getId().equals(questionId)) {
            throw new RuntimeException(
                    "Answer does not belong to this question");
        }

        if (answerBlock.getType() != AnswerBlockType.TEXT) {
            throw new RuntimeException(
                    "Answer is not a TEXT answer");
        }

        answerBlock.setContent(request.getContent());
        answerBlock.setDisplayOrder(request.getDisplayOrder());

        return answerBlockRepository.save(answerBlock);
    }


    // =========================
    // UPDATE TABLE ANSWER
    // =========================

    public AnswerBlock updateTableAnswer(
            Long questionId,
            Long answerId,
            TableAnswerRequest request) {

        if (!questionRepository.existsById(questionId)) {
            throw new RuntimeException("Question not found");
        }

        AnswerBlock answerBlock = answerBlockRepository
                .findById(answerId)
                .orElseThrow(() ->
                        new RuntimeException("Answer not found"));

        if (!answerBlock.getQuestion().getId().equals(questionId)) {
            throw new RuntimeException(
                    "Answer does not belong to this question");
        }

        if (answerBlock.getType() != AnswerBlockType.TABLE) {
            throw new RuntimeException(
                    "Answer is not a TABLE answer");
        }

        // Update display order
        answerBlock.setDisplayOrder(request.getDisplayOrder());

        answerBlockRepository.save(answerBlock);


        // Find existing table
        AnswerTable table = answerTableRepository
                .findByAnswerBlockId(answerId)
                .orElseThrow(() ->
                        new RuntimeException("Table not found"));


        // =========================
        // DELETE OLD CELLS
        // =========================

        List<TableRow> oldRows =
                tableRowRepository
                        .findByTableIdOrderByDisplayOrderAsc(
                                table.getId()
                        );

        for (TableRow row : oldRows) {

            List<TableCell> cells =
                    tableCellRepository
                            .findByRowIdOrderByColumnOrderAsc(
                                    row.getId()
                            );

            tableCellRepository.deleteAll(cells);
        }


        // =========================
        // DELETE OLD ROWS
        // =========================

        tableRowRepository.deleteAll(oldRows);


        // =========================
        // DELETE OLD COLUMNS
        // =========================

        List<TableColumn> oldColumns =
                tableColumnRepository
                        .findByTableIdOrderByDisplayOrderAsc(
                                table.getId()
                        );

        tableColumnRepository.deleteAll(oldColumns);


        // =========================
        // CREATE NEW COLUMNS
        // =========================

        List<String> columns = request.getColumns();

        for (int i = 0; i < columns.size(); i++) {

            TableColumn tableColumn = new TableColumn();

            tableColumn.setTable(table);
            tableColumn.setColumnName(columns.get(i));
            tableColumn.setDisplayOrder(i + 1);

            tableColumnRepository.save(tableColumn);
        }


        // =========================
        // CREATE NEW ROWS + CELLS
        // =========================

        List<List<String>> rows = request.getRows();

        for (int rowIndex = 0;
             rowIndex < rows.size();
             rowIndex++) {

            TableRow tableRow = new TableRow();

            tableRow.setTable(table);
            tableRow.setDisplayOrder(rowIndex + 1);

            TableRow savedRow =
                    tableRowRepository.save(tableRow);


            List<String> cells = rows.get(rowIndex);

            for (int columnIndex = 0;
                 columnIndex < cells.size();
                 columnIndex++) {

                TableCell tableCell = new TableCell();

                tableCell.setRow(savedRow);
                tableCell.setContent(cells.get(columnIndex));
                tableCell.setColumnOrder(columnIndex + 1);

                tableCellRepository.save(tableCell);
            }
        }

        return answerBlock;
    }


    // =========================
    // DELETE ANSWER
    // =========================

    public String deleteAnswer(
            Long questionId,
            Long answerId) {

        if (!questionRepository.existsById(questionId)) {
            throw new RuntimeException("Question not found");
        }

        AnswerBlock answerBlock = answerBlockRepository
                .findById(answerId)
                .orElseThrow(() ->
                        new RuntimeException("Answer not found"));

        if (!answerBlock.getQuestion().getId().equals(questionId)) {
            throw new RuntimeException(
                    "Answer does not belong to this question");
        }

        answerBlockRepository.delete(answerBlock);

        return "Answer deleted successfully";
    }


    // =========================
    // GET ANSWERS
    // =========================

    public List<AnswerResponse> getAnswers(Long questionId) {

        if (!questionRepository.existsById(questionId)) {

            throw new RuntimeException("Question not found");
        }

        List<AnswerBlock> blocks =
                answerBlockRepository
                        .findByQuestionIdOrderByDisplayOrderAsc(questionId);

        return blocks.stream()
                .map(this::convertToResponse)
                .toList();
    }


    // =========================
    // CONVERT ANSWER TO RESPONSE
    // =========================

    private AnswerResponse convertToResponse(
            AnswerBlock block) {

        // TEXT answer
        if (block.getType() == AnswerBlockType.TEXT) {

            return new AnswerResponse(
                    block.getId(),
                    block.getType(),
                    block.getContent(),
                    block.getDisplayOrder(),
                    null
            );
        }


        // TABLE answer
        AnswerTable table = answerTableRepository
                .findByAnswerBlockId(block.getId())
                .orElseThrow(() ->
                        new RuntimeException("Table not found"));


        // Get columns
        List<TableColumn> columns =
                tableColumnRepository
                        .findByTableIdOrderByDisplayOrderAsc(
                                table.getId()
                        );


        // Get rows
        List<TableRow> rows =
                tableRowRepository
                        .findByTableIdOrderByDisplayOrderAsc(
                                table.getId()
                        );


        // Convert columns
        List<String> columnNames =
                columns.stream()
                        .map(TableColumn::getColumnName)
                        .toList();


        // Convert rows + cells
        List<List<String>> rowData =
                rows.stream()
                        .map(row ->
                                tableCellRepository
                                        .findByRowIdOrderByColumnOrderAsc(
                                                row.getId()
                                        )
                                        .stream()
                                        .map(TableCell::getContent)
                                        .toList()
                        )
                        .toList();


        TableResponse tableResponse =
                new TableResponse(
                        columnNames,
                        rowData
                );


        return new AnswerResponse(
                block.getId(),
                block.getType(),
                null,
                block.getDisplayOrder(),
                tableResponse
        );
    }
}