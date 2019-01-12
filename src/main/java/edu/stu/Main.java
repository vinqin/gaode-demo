package edu.stu;

import edu.stu.bean.Result;
import edu.stu.util.Tools;

import java.util.List;

public class Main {

    private static int i;

    public static void main(String[] args) {

        //Tools.getInstance().writeExcel();

        Tools tools = Tools.getInstance();
        List<Result> results = tools.getResults();

        int threadCounts = results.size() / 1000; // 每条线程均分1000个下载任务
        for (i = 0; i < threadCounts; i++) {
            new Thread(() -> {
                int startIndex;
                int endIndex;
                synchronized (Main.class) {
                    startIndex = i * 1000;
                    endIndex = (i + 1) * 1000 < results.size() ? (i + 1) * 1000 : results.size();
                }
                tools.requestGaoDe(startIndex, endIndex);
            }).start();
        }

        int surplus = results.size() - 1000 * (results.size() / 1000);
        if (surplus > 0) {
            int startIndex = 1000 * (results.size() / 1000);
            int endIndex = results.size();
            tools.requestGaoDe(startIndex, endIndex);
        }


    }
}
