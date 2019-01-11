package edu.stu.xlsx;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JExcelAPIDemo {

    private final URL XLS_SRC1_URL = this.getClass().getResource("/src_test.xls");

    private final File XLS_SRC = new File(XLS_SRC1_URL.toURI());


    public JExcelAPIDemo() throws URISyntaxException {
    }


    private final String addressPrefix = "汕头市金平区广厦新城";

    private List<String> addressList = new ArrayList<>();

    //如果读一个excel，需要知道它有多少行和多少列
    @Test
    public void testReadXlsFile() throws Exception {
        Workbook book = Workbook.getWorkbook(XLS_SRC);
        // 获得第一个工作表对象
        Sheet sheet = book.getSheet(0);
        // 得到第一列第一行的单元格
        int columNum = sheet.getColumns();// 得到列数
        int rowNum = sheet.getRows();// 得到行数
        String sheetName = sheet.getName();
        System.out.println(sheetName + "共有" + rowNum + "行, " + columNum + "列");
        for (int i = 1;
             i < rowNum;
             i++) {
            // 从第二行开始，循环进行读写
            for (int j = 0;
                 j < columNum;
                 j++) {
                Cell cell1 = sheet.getCell(j, i); // 获取单元格
                String content = cell1.getContents();
                if ("".equals(content.trim())) {
                    content = "null";
                } else if (1 == j) {
//                    String address = addressPrefix + content.trim();
                    String address = content.trim();
                    addressList.add(address);
                }

                //System.out.print(content);
                //System.out.print("\t");
            }
            //System.out.println();
        }
        book.close();
    }

    public List<String> getAddressList() {
        try {
            testReadXlsFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressList;
    }
}
