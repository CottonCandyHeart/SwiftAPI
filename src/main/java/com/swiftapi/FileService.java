package com.swiftapi;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private List<List<String>> list;

    public void readFile(String filePath) throws IOException, InvalidFormatException {
        FileInputStream file = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        list = new ArrayList<>();

        for (Row row : sheet){
            List<String> rowList = new ArrayList<>();
            for (Cell cell : row){
                rowList.add(cell.getStringCellValue());
            }
            list.add(rowList);
        }

        list.remove(0);

        workbook.close();
        file.close();

    }

    public void createTables(){

    }
}
