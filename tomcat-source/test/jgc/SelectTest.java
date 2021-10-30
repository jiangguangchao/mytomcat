package jgc;

import org.junit.Test;

import java.nio.channels.Selector;

/**
 * @program: Tomcat
 * @description:
 * @author:
 * @create: 2021-10-14 16:39
 */
public class SelectTest {

    @Test
    public void f1() {
        try {
            Selector selector = Selector.open();
            while(true) {
//                selector.wakeup();
                int a = selector.select(3000);
                System.out.println("a: " + a);
            }

        } catch (Exception e) {

        }
    }
}
