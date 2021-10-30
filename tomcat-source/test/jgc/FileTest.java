package jgc;

import org.apache.tools.ant.util.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * @program: Tomcat
 * @description:
 * @author:
 * @create: 2021-10-13 14:13
 */
public class FileTest {


    @Test
    public void f1() {
        System.out.println("bbbbbbbbbbb");
        String sourceFile = "D:\\pic\\bg-middle.png";
        String destPath = "";
        int begin = 20;
        try {
            File file = new File(sourceFile);
            String fileName = file.getName();
            String[] names = fileName.split("\\.");
            String dir = file.getParentFile().getAbsolutePath();
            System.out.println("文件名称：" + names[0] + ",  " + names[1]);
            System.out.println("路径：" + dir);
            FileUtils fUtils = FileUtils.newFileUtils();
            while(true) {
                String destFile = dir + File.separator + names[0] + begin + "." + names[1];
                fUtils.copyFile(sourceFile, destFile);
                System.out.println("<img src=\"/img/" + names[0] + begin + "." + names[1] + "\" />");
                begin++;
                if (begin > 50) {
                    break;
                }
            }
//            fUtils.copyFile(sourceFile, destPath);
        } catch (Exception e) {
            System.out.println("文件操作异常" + e);
        }
    }
}
