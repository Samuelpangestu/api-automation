package com.indodax.helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel Reader Utility for Data-Driven Testing
 * Reads test data from Excel files (.xlsx)
 */
public class ExcelReader {
    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);
    private Workbook workbook;
    private Sheet sheet;
    private String filePath;

    public ExcelReader(String filePath) {
        this.filePath = filePath;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            logger.info("Excel file loaded successfully: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to load Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to load Excel file: " + filePath, e);
        }
    }

    /**
     * Select sheet by name
     */
    public void selectSheet(String sheetName) {
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException("Sheet not found: " + sheetName);
        }
        logger.info("Sheet selected: {}", sheetName);
    }

    /**
     * Select sheet by index
     */
    public void selectSheet(int sheetIndex) {
        sheet = workbook.getSheetAt(sheetIndex);
        logger.info("Sheet selected by index: {}", sheetIndex);
    }

    /**
     * Get total number of rows in the current sheet
     */
    public int getRowCount() {
        return sheet.getLastRowNum() + 1;
    }

    /**
     * Get total number of columns in a specific row
     */
    public int getColumnCount(int rowNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return 0;
        }
        return row.getLastCellNum();
    }

    /**
     * Get cell value as String
     */
    public String getCellData(int rowNum, int colNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                return "";
            }

            Cell cell = row.getCell(colNum);
            if (cell == null) {
                return "";
            }

            return getCellValueAsString(cell);
        } catch (Exception e) {
            logger.error("Error reading cell data at row: {}, col: {}", rowNum, colNum, e);
            return "";
        }
    }

    /**
     * Convert cell value to String based on cell type
     */
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Get all data from sheet as List of Maps
     * First row is treated as headers
     */
    public List<Map<String, String>> getAllDataAsMap() {
        List<Map<String, String>> dataList = new ArrayList<>();

        if (sheet.getLastRowNum() < 1) {
            logger.warn("Sheet has no data rows");
            return dataList;
        }

        // Get headers from first row
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            headers.add(getCellData(0, i));
        }

        // Get data rows
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Map<String, String> rowData = new HashMap<>();
            for (int j = 0; j < headers.size(); j++) {
                rowData.put(headers.get(j), getCellData(i, j));
            }
            dataList.add(rowData);
        }

        logger.info("Retrieved {} rows of data from sheet", dataList.size());
        return dataList;
    }

    /**
     * Get specific row data as Map
     */
    public Map<String, String> getRowDataAsMap(int rowNum) {
        Map<String, String> rowData = new HashMap<>();

        // Get headers from first row
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            headers.add(getCellData(0, i));
        }

        // Get data from specified row
        for (int i = 0; i < headers.size(); i++) {
            rowData.put(headers.get(i), getCellData(rowNum, i));
        }

        return rowData;
    }

    /**
     * Get data by test case name or Test ID
     */
    public Map<String, String> getTestDataByName(String testCaseName) {
        List<Map<String, String>> allData = getAllDataAsMap();
        for (Map<String, String> rowData : allData) {
            // Check both "Test Case" and "Test ID" columns for backward compatibility
            if (testCaseName.equals(rowData.get("Test Case")) ||
                testCaseName.equals(rowData.get("Test ID"))) {
                logger.info("Test data found for: {}", testCaseName);
                return rowData;
            }
        }
        throw new RuntimeException("Test case not found: " + testCaseName);
    }

    /**
     * Close workbook
     */
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
                logger.info("Excel workbook closed");
            }
        } catch (IOException e) {
            logger.error("Error closing workbook", e);
        }
    }
}
