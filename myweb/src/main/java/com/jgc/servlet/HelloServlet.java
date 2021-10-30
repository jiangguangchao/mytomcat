package com.jgc.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class HelloServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("检测到post请求");
        //response.setHeader("Access-Control-Allow-Origin","http://localhost:8080");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("检测到get请求");

        //设置编码格式，如果没有设置，tomcat默认的编码格式是ISO-8859-1
        response.setCharacterEncoding("UTF-8");

        //同时设置content type和编码格式，所以这一行代码包含了上一行代码的功能，也就是说，
        // 有了这一行代码，上面的一行代码可以不用写。这行代码的作用是让浏览器用哪种编码格式显示，
        // 有些浏览器能自动识别，比如360浏览器，只需要上面一行代码就行，也就是response.setCharacterEncoding("UTF-8");
        // 就能自动将浏览器显示的编码格式设置为utf-8，从而正常显示文字。但是谷歌浏览器比如有下面这行代码，才能正常显示文字。
        // 所以用下面这行代码就可以了，适用所有浏览器。
        response.setContentType("text/html; charset=UTF-8");
        Writer writer = response.getWriter();
        writer.write("你好 java1233333 hello");
        writer.flush();
        writer.close();
//        response.setHeader("Access-Control-Allow-Origin","http://localhost:8080");

//        System.out.println(Thread.currentThread().getId());
    }
}
