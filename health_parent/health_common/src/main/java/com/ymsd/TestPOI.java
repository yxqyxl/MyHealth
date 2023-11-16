package com.ymsd;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestPOI {
    @Test
    public void test() throws Exception {
        //创建要读取工作薄对象
        XSSFWorkbook workbook = new XSSFWorkbook("E:\\hello.xlsx");
        //获取工作表，既可以根据工作表的顺序读取，也可以根据工作表的名称获取
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row : sheetAt) {
            //遍历行对应的单元格
            for (Cell cell : row) {
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
            workbook.close();
        }
    }

    @Test
    public void test02() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("6666");
        list.add("不错呀");
//        1.创建Excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
//        2.创建一个sheet
        HSSFSheet sheet = workbook.createSheet();
//        3.创建第一行
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = null;
        for (int i = 0; i < list.size(); i++) {
            //4.创建cell
            cell = row.createCell(i);
            String s = list.get(i);
            //5.往cell中填入数据
            cell.setCellValue(s);
        }

        File file = new File("E:/poi_test.xls");
        System.out.println(file);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {

        }
    }
}
