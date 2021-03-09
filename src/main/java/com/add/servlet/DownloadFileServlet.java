package com.add.servlet;


import com.add.dao.FileDao;
import com.add.dao.FileDownloadDao;
import com.add.util.FileUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/downloadFile")
public class DownloadFileServlet extends HttpServlet {
    private FileDao fileDao = new FileDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        //获取要下载文件的路径
        Integer fileId = Integer.parseInt(req.getParameter("fileId"));
        String filePath = FileUtil.ROOT + req.getParameter("filePath");
        System.out.println(filePath);
        System.out.println(fileId);

        //下载文件的路径和下载文件的输入流
        File downloadFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(filePath);
        System.out.println("file " + filePath);
        //获取servletContent
        ServletContext context = getServletContext();
        //获取文件类型
        String mineType = context.getMimeType(filePath);
        //System.out.println("mineType = " + mineType);
        if (mineType == null){
                mineType = "application/octet-stream";
        }
        //设置响应
        resp.setContentType(mineType);
        resp.setContentLength((int) downloadFile.length());


        //下载
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        resp.setHeader(headerKey, headerValue);


        //获取response的输出流
        ServletOutputStream outputStream = resp.getOutputStream();
        byte[] buffer = new byte[4096];
        int byteRead = -1;
        while ((byteRead = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0,byteRead);
        }
        //下载文件次数+1
        fileDao.addDownloadCount(fileId);
        //关闭资源
        inputStream.close();
        outputStream.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //获取下载的url
//        String file_url = req.getParameter("url");
//        req.setCharacterEncoding("UTF-8");
//        resp.setContentType("text/html;charset=UTF-8");
//        //插入数据库
//        //boolean flag = fileDownloadDao.download(file_url);
//        boolean flag = true;
//
//        //响应数据
//        PrintWriter pw = resp.getWriter();
//        if (flag){
//            pw.println("下载请求收到");
//            System.out.println("下载请求已经收到");
//        }
//        else {
//            pw.println("下载请求接收失败");
//            System.out.println("下载请求失败");
//        }
//        pw.flush();pw.close();
        doPost(req,resp);
    }
}
