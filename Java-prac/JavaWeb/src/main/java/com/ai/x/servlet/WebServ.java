package com.ai.x.servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;


// WebServlet注解表示这是一个Servlet，并映射到地址/:
@WebServlet(urlPatterns = "/test")
public class WebServ extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 设置响应类型:
        resp.setContentType("text/html; charset=utf-8");

        String name = req.getParameter("name");
        if (name == null) {
            name = "赵小小";
        }

        // 获取输出流:
        PrintWriter pw = resp.getWriter();
        // 写入响应:
        pw.write("<h1>你好,"  +  name +  "世界!</h1>");
        // 最后不要忘记flush强制输出:
        pw.flush();
    }
}
