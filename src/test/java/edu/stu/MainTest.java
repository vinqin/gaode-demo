package edu.stu;

import com.google.gson.Gson;
import edu.stu.bean.Result;
import edu.stu.domain.SearchResult;
import edu.stu.util.HttpDemo;
import edu.stu.util.Tools;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class MainTest {

    Gson gson = new Gson();

    @Test
    public void test1() throws IOException, URISyntaxException {

        String templateJsonString = Tools.readTextFromClassPath("/template.json");

        SearchResult result = gson.fromJson(templateJsonString, SearchResult.class);

        System.out.println(result);

    }

    @Test
    public void test2() throws Exception {
        String xmlString = HttpDemo.requestForHttp("南香渔港(总店)");
        System.out.println(xmlString);
        File xmlFile = new File("/home/vinqin/IdeaProjects/gaode-demo/src/main/resources/template.xml");
        FileUtils.writeStringToFile(xmlFile, xmlString, StandardCharsets.UTF_8);
    }

    @Test
    public void test3() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(edu.stu.bean.SearchResult.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        edu.stu.bean.SearchResult result = (edu.stu.bean.SearchResult) jaxbUnmarshaller.unmarshal(new File("/home" +
                "/vinqin/IdeaProjects/gaode-demo/src/main/resources/template.xml"));
        System.out.println(result);
    }

    // 测试读取xlsx文件
    @Test
    public void test4() {
        String dirPath = "/home/vinqin/IdeaProjects/gaode-demo/excel-src/大众点评数据/零售/";
        File rootDir = new File(dirPath);
        readExcel(rootDir, rootDir.getName());
    }

    // 测试写入已存在的xlsx文件
    @Test
    public void test5() throws IOException {
        Result result = new Result();
        result.setCity("汕头");
        result.setCategory("餐饮");
        result.setStoreName("天天鲜牛肉(鮀浦店)");
        result.setLongitude("116.645441,23.407354");
        result.setGrade("三星");
        result.setDetailedAddress("广东省汕头市蓬洲西埕后工业区大学路(大学路255号");
        result.setUrl("https://www.xvideos.com");
        writeAnRowDataToExcel(result);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(targetFile);
        targetWorkbook.write(fileOut);
        fileOut.close();
    }

    private static final File targetFile = new File("/home/vinqin/IdeaProjects/gaode-demo/excel-src/test_result.xlsx");

    // we create an XSSF Workbook object for our XLSX Excel File
    private XSSFWorkbook targetWorkbook;

    // we get first sheet
    private XSSFSheet targetSheet;

    {
        try {
            FileInputStream fis = new FileInputStream(targetFile);
            targetWorkbook = new XSSFWorkbook(fis);
            targetSheet = targetWorkbook.getSheetAt(0);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeAnRowDataToExcel(Result rowResult) {

        // Create a Row
        Row row = targetSheet.createRow(2);
        row.createCell(0).setCellValue(rowResult.getCity());
        row.createCell(1).setCellValue(rowResult.getCategory());
        row.createCell(2).setCellValue(rowResult.getStoreName());
        row.createCell(3).setCellValue(rowResult.getLongitude());
        row.createCell(4).setCellValue(rowResult.getGrade());
        row.createCell(5).setCellValue(rowResult.getDetailedAddress());
        row.createCell(6).setCellValue(rowResult.getUrl());


    }

    private void readExcel(File rootDir, String categoryPrefix) {
        File[] files = rootDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    readExcel(file, categoryPrefix + "/" + file.getName());
                } else {
                    Result rowResult = new Result();
                    rowResult.setCategory(categoryPrefix);
                    try {
                        readExcel(file, rowResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void readExcel(File excelFile, Result rowResult) throws Exception {
        FileInputStream fis = new FileInputStream(excelFile);

        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        // we get first sheet
        XSSFSheet sheet = workbook.getSheetAt(0);


        // we iterate on rows
        Iterator<Row> rowIt = sheet.iterator();

        int count = 0;

        while (rowIt.hasNext()) {
            if (count > 5) {
                break;
            }

            Row row = rowIt.next();

            // iterate on cells for the current row
            Iterator<Cell> cellIterator = row.cellIterator();

            System.out.print(rowResult.getCategory() + "; ");

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                System.out.print(cell.toString() + "; ");
            }

            System.out.println();
            count++;
        }

        System.out.println("==========");
        workbook.close();
        fis.close();
    }

}
