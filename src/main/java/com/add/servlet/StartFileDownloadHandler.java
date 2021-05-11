package com.add.servlet;

import com.add.thread.FileDownloadHandleThread;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/startFileDownloadHandlerBut")
public class StartFileDownloadHandler extends AbstractBaseServlet{
    private int status = 0;//0表示未开启，1表示已经开启

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (status == 0) {
            new Thread(new FileDownloadHandleThread()).start();
            status = 1;
        }
        System.out.println(status);
        return status;
    }
}
