package jgc;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Tomcat
 * @description:
 * @author:
 * @create: 2021-10-25 08:56
 */
public class JHSDBTest {

    private byte[] bytes = new byte[60 * 1024];

    public static void foo(int num) throws InterruptedException {
        Thread.sleep(4000);
        List<JHSDBTest> tests = new ArrayList<JHSDBTest>();
        for (int i = 0; i < num; i++) {
            JHSDBTest test = new JHSDBTest();
            tests.add(test);
            Thread.sleep(50000);
        }

        System.gc();
    }


    public static void main(String[] args) throws InterruptedException {
        JHSDBTest.foo(1000);
    }
}
