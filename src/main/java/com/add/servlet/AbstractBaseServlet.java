package com.add.servlet;

import com.add.exception.AppException;
import com.add.model.JsonResponse;
import com.add.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public abstract class AbstractBaseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码格式
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        //设置响应体格式
        resp.setContentType("application/json");
        //Session会话管理，除登录和注册接口，其他都需要登录后访问
        //TODO
        JsonResponse json = new JsonResponse();
        try{
            //调用子类重写的方法
            Object obj = process(req,resp);
            json.setData(obj);
            json.setSuccess(true);
        }catch (Exception e){
            //异常处理：JDBC异常SQLException，JSON处理的异常，自定义异常
            e.printStackTrace();
            String mes = "未知错误";
            String code = "UNKNOWN";
            if (e instanceof AppException) {
                code = ((AppException) e).getCode();
                mes =  e.getMessage();
            }
            json.setCode(code);json.setMessage(mes);

        }

        //输出到页面
        PrintWriter pw = resp.getWriter();
        pw.println(JsonUtil.serialize(json));
        pw.flush();pw.close();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    //子类要重写的方法
    protected abstract Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
