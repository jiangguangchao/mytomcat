package org.apache.jgctest;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @program: Tomcat
 * @description:
 * @author:
 * @create: 2021-07-16 09:07
 */
public class MyHttp {

    public void doGet(String url) {
        HttpURLConnection connection = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                System.out.println("请求成功");
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyHttp http = new MyHttp();
        int count = 10;
        Long ss = System.currentTimeMillis();
        System.out.println("开始请求前时间:" + ss);
        for(int i =1;i<=count;i++) {
            Long s = System.currentTimeMillis();
            System.out.println("请求开始毫秒数：" + s);
            http.doGet("http://localhost:8080/web/hello");
            Long e = System.currentTimeMillis();
            System.out.println("请求结束毫秒数：" + e);
            System.out.println("第" + i + "次请求用时" + (e-s) + "毫秒");
        }
        Long ee = System.currentTimeMillis();
        long time = ee - ss;
        long every = time/count;
        System.out.println(count + "次请求结束时间:" + ee + ", " + count + "次请求用时：" + time + ",  平均每次请求耗时：" + every);

    }
}
