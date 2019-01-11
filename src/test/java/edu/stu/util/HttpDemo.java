package edu.stu.util;

import edu.stu.xlsx.JExcelAPIDemo;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpDemo {

    //    public static final String URL = "http://restapi.amap.com/v3/geocode/geo";
    public static final String URL = "https://restapi.amap.com/v3/assistant/inputtips";

    private static final String KEY = "ea12dc25df82114c19bc0870c5a348ed";

    /**
     * 准备发送到高德API的HTTP POST参数
     *
     * @return 高德API传回来的JSON字符串
     */
    public static String requestForHttp(String storeName) throws Exception {
        String result = "";
        final Map<String, String> param = new HashMap<>();
        param.put("key", KEY);
        param.put("keywords", storeName);
        param.put("city", "汕头");
        param.put("citylimit", "true"); // 限制结果为汕头
        param.put("output", "XML");
        //System.out.println("请求的参数：" + param.toString());

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
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");

            EntityUtils.consume(httpEntity);//释放资源
            httpClient.close();
            httpResponse.close();
        } catch (IOException e) {

        }
        //System.out.println("返回的参数：" + result);
        return result;
    }

    public static void main(String[] args) throws Exception {
        JExcelAPIDemo jExcelAPIDemo = new JExcelAPIDemo();
        List<String> addressList = jExcelAPIDemo.getAddressList();
        java.net.URL destJSONUrl = jExcelAPIDemo.getClass().getResource("/result3.json");
        File resultJSON = new File(destJSONUrl.toURI());

        downloadJsonData(addressList, resultJSON);
    }

    private static void downloadJsonData(List<String> addresses, File resultJSON) throws Exception {
        if (addresses == null || addresses.size() == 0) {
            return;
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(resultJSON), StandardCharsets.UTF_8));
        String jsonPrefix = "{\"geocoding\": [";
        jsonPrefix = jsonPrefix + requestForHttp(addresses.get(0));
        writer.write(jsonPrefix);

        for (int i = 1;
             i < addresses.size();
             i++) {
            writer.write(",");
            System.out.println(addresses.get(i));
            writer.write(requestForHttp(addresses.get(i)));
        }
        String jsonSuffix = "]}";
        writer.write(jsonSuffix);

        writer.flush();
        writer.close();
    }

}
