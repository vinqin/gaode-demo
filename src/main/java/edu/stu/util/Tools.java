package edu.stu.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Tools {

    public static String readTextFromClassPath(String classPath) throws URISyntaxException, IOException {
        URL url = Tools.class.getResource(classPath);
        File file = new File(url.toURI());
        return FileUtils.readFileToString(file, "UTF-8");
    }

}
