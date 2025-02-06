package com.swiftapi.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    public List<List<String>> readFile(String filePath) throws IOException {
        try{
            // OPEN FILE
            //FileInputStream file = new FileInputStream(filePath);
            InputStream file = new ClassPathResource("Interns_2025_SWIFT_CODES.xlsx").getInputStream();
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            List<List<String>> list = new ArrayList<>();

            // READ FILE
            for (Row row : sheet){
                List<String> rowList = new ArrayList<>();
                for (Cell cell : row){
                    rowList.add(cell.getStringCellValue());
                }
                list.add(rowList);
            }

            // REMOVE TITLE LINE
            list.remove(0);

            workbook.close();
            file.close();

            return list;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filePath, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + filePath, e);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Invalid Excel format: " + e.getMessage(), e);
        }
    }
}
