package edu.stu.util;

import edu.stu.bean.Result;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Tools {

    private static final File targetFile =
            new File("/home/vinqin/IdeaProjects/gaode-demo/excel-src/dianping_st_1.xlsx");

    private static final File sourceDirRootPath = new File("/home/vinqin/IdeaProjects/gaode-demo/excel-src/大众点评数据/");

    private static final String targetXMLSDir = "/home/vinqin/IdeaProjects/gaode-demo/excel-src/xmls_3/";

    private static Tools tools = new Tools();

    private Tools() {
    }

    public static Tools getInstance() {
        return tools;
    }

    public static String readTextFromClassPath(String classPath) throws URISyntaxException, IOException {
        URL url = Tools.class.getResource(classPath);
        File file = new File(url.toURI());
        return FileUtils.readFileToString(file, "UTF-8");
    }


    // we create an XSSF Workbook object for our XLSX Excel File
    private XSSFWorkbook targetWorkbook;

    // we get first sheet
    private XSSFSheet targetSheet;

    // 目标表的总行数，从第二行开始
    private int counts = 1;

    // 加载目标表到内存中
    private void init() {
        try {
            FileInputStream fis = new FileInputStream(targetFile);
            targetWorkbook = new XSSFWorkbook(fis);
            targetSheet = targetWorkbook.getSheetAt(0);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeExcel() {
        init();

        readAndWriteExcel(sourceDirRootPath, "");

        try {
            // Write the output to a file
            FileOutputStream fos = new FileOutputStream(targetFile);
            targetWorkbook.write(fos);
            targetWorkbook.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAndWriteExcel(File rootDir, String categoryPrefix) {
        File[] files = rootDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String category;
                    if ("".equals(categoryPrefix)) {
                        category = file.getName();
                    } else {
                        category = categoryPrefix + "/" + file.getName();
                    }
                    readAndWriteExcel(file, category);
                } else {
                    try {
                        readOneExcelAndWriteToTarget(file, categoryPrefix);
                    } catch (Exception e) {
                        System.out.println("出错文件是：" + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void readOneExcelAndWriteToTarget(File excelFile, String category) throws Exception {
        FileInputStream fis = new FileInputStream(excelFile);

        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        // we get first sheet
        XSSFSheet sheet = workbook.getSheetAt(0);

        // we iterate on rows
        Iterator<Row> rowIt = sheet.iterator();

        boolean isRepast = category.contains("餐饮"); // 是否为餐饮分类

        boolean isFirstLine = true;

        while (rowIt.hasNext()) {
            if (isFirstLine) {
                isFirstLine = false;
                rowIt.next();
                // 跳过第一行
                continue;
            }

            counts++;
            Result rowResult = new Result();
            rowResult.setCategory(category);
            Row row = rowIt.next();
            // iterate on cells for the current row
            Iterator<Cell> cellIterator = row.cellIterator();
            for (int i = 0; cellIterator.hasNext(); i++) {
                if (i > 2 && !isRepast) {
                    break;
                }
                Cell cell = cellIterator.next();
                String cellValue = cell.toString();

                if (isRepast) {
                    switch (i) {
                        case 1:
                            rowResult.setUrl(cellValue);
                            break;
                        case 2:
                            rowResult.setStoreName(cellValue);
                            break;
                        case 9:
                            rowResult.setGrade(cellValue);
                            break;
                    }
                } else {
                    switch (i) {
                        case 0:
                            rowResult.setUrl(cellValue);
                            break;
                        case 1:
                            rowResult.setStoreName(cellValue);
                            break;
                        case 2:
                            rowResult.setGrade(cellValue);
                            break;
                    }
                }
            }

            writeAnRowDataToTarget(rowResult);
        }

        workbook.close();
        fis.close();
    }


    private void writeAnRowDataToTarget(Result rowResult) {
        // Create a Row
        Row row = targetSheet.createRow(counts);
        row.createCell(0).setCellValue(rowResult.getCity());
        row.createCell(1).setCellValue(rowResult.getCategory());
        row.createCell(2).setCellValue(rowResult.getStoreName());
        row.createCell(3).setCellValue(rowResult.getLongitude());
        row.createCell(4).setCellValue(rowResult.getGrade());
        row.createCell(5).setCellValue(rowResult.getDetailedAddress());
        row.createCell(6).setCellValue(rowResult.getUrl());
    }


    // 高德 输入提示 API接口
    private static final String URL = "https://restapi.amap.com/v3/assistant/inputtips";

    private static final String KEY = "ea12dc25df82114c19bc0870c5a348ed";


    /**
     * 准备发送到高德API的HTTP POST参数
     *
     * @return 高德API传回来的JSON字符串
     */
    public String requestForHttp(String _key, String city, String storeName) throws Exception {
        String result = "";
        Map<String, String> param = new HashMap<>();
        param.put("keywords", storeName);
        param.put("city", city);
        param.put("key", KEY);
        param.put("citylimit", "true"); // 限制结果为某个具体的城市
        param.put("output", "XML");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL);
        List<BasicNameValuePair> params = new ArrayList<>();
        Iterator<Map.Entry<String, String>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            if (value != null) {
                params.add(new BasicNameValuePair(key, value));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        /*HttpResponse*/
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        result = EntityUtils.toString(httpEntity, "utf-8");

        EntityUtils.consume(httpEntity);//释放资源
        httpClient.close();
        httpResponse.close();
        //System.out.println("返回的参数：" + result);
        return result;
    }

    private List<Result> results;

    public synchronized List<Result> getResults() {
        if (results != null) {
            return results;
        }

        results = new ArrayList<>();
        readResultsFromExcel();
        return results;
    }

    private void readResultsFromExcel() {
        init();

        Iterator<Row> rowIt = targetSheet.iterator();
        boolean isFirstRow = true;

        while (rowIt.hasNext()) {
            Row row = rowIt.next();
            if (isFirstRow) {
                isFirstRow = false;
                continue;
            }

            Result result = new Result();
            result.setCity(row.getCell(0).toString());
            result.setCategory(row.getCell(1).toString());
            result.setStoreName(row.getCell(2).toString());
            result.setGrade(row.getCell(4).toString());
            result.setUrl(row.getCell(6).toString());
            results.add(result);
        }
    }

    /**
     * 向高德API发送查询请求，并将查询结果保持到xml里和excel里面
     *
     * @param startIndex 查询的开始索引
     * @param endIndex   查询的结束索引（不包含）
     */
    public void requestGaoDe(int startIndex, int endIndex) {
        List<Result> results = getResults();

        for (int i = startIndex; i < endIndex; i++) {
            Result re = results.get(i);
            try {
                String xmlString = requestForHttp("", re.getCity(), re.getStoreName());

                JAXBContext jaxbContext = JAXBContext.newInstance(edu.stu.bean.SearchResult.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(xmlString);
                edu.stu.bean.SearchResult searchResult = (edu.stu.bean.SearchResult) jaxbUnmarshaller.unmarshal(reader);

                if ("0".equals(searchResult.getStatus())) {
                    System.out.println("高德API查询失败！");
                    System.out.println(xmlString);
                    return;
                }

                File xmlFile = new File(targetXMLSDir + i + "_" + re.getStoreName() + ".xml");
                FileUtils.writeStringToFile(xmlFile, xmlString, StandardCharsets.UTF_8);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("API查询出错!");
                //System.exit(1);
                Thread.currentThread().interrupt();
            }

        }
    }

    public void requestGaoDe() {
        requestGaoDe(0, getResults().size());
    }

}
