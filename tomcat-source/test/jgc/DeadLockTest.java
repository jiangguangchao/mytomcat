package jgc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @program: Tomcat
 * @description:
 * @author:
 * @create: 2021-10-25 10:26
 */
public class DeadLockTest {

    private static Object a = new Object();
    private static Object b = new Object();

    public static void foo (Object obj1, Object obj2, String threadName) {
        Thread thread = new Thread(() -> {
            synchronized(obj1) {
                System.out.println("线程" + threadName + "已获取" + obj1 + "的锁, 准备获取" + obj2 + "的锁");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized(obj2) {
                    System.out.println("线程" + threadName + "已获取" + obj2 + "的锁");
                }
            }
        }, threadName);

        thread.start();

    }

    public static void main(String[] args) {
        DeadLockTest.foo(a,b, "t1");
        DeadLockTest.foo(b,a, "t2");
    }
}
