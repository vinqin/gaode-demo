package edu.stu;

import com.google.gson.Gson;
import edu.stu.domain.SearchResult;
import edu.stu.util.HttpDemo;
import edu.stu.util.Tools;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

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
        edu.stu.bean.SearchResult result = (edu.stu.bean.SearchResult) jaxbUnmarshaller.unmarshal(new File("/home/vinqin/IdeaProjects/gaode-demo/src/main/resources/template.xml"));
        System.out.println(result);
    }
}
