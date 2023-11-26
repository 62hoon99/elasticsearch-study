package com.elasticsearch.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class RestaurantFileExtractor {
    public static List<Restaurant> extract(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(file.getBytes()))) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();

            List<Restaurant> restaurants = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                restaurants.add(Restaurant.builder()
                        .name(row.getCell(0).getStringCellValue())
                        .category1(row.getCell(1).getStringCellValue())
                        .category2(row.getCell(2).getStringCellValue())
                        .category3(row.getCell(3).getStringCellValue())
                        .state(row.getCell(4).getStringCellValue())
                        .city(row.getCell(5).getStringCellValue())
                        .description(row.getCell(6).getStringCellValue())
                        .build());
            }

            return restaurants;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IOException");
            throw new IllegalArgumentException();
        }
    }
}
