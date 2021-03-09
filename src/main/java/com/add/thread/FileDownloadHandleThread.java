package com.add.thread;

import com.add.dao.FileDao;
import com.add.model.FileInfo;
import com.add.util.DBUtil;

import java.sql.*;
import java.util.Date;

public class FileDownloadHandleThread implements Runnable{
    private FileDao fileDao = new FileDao();

    @Override
    public void run() {


            while (true) {
                //执行sql
                FileInfo file = fileDao.queryOneDownloadTask();
                //有下载任务
                if (file != null){
                    //修改下载状态为正在下载
                    file.setDownloadStatus(1);
                    fileDao.updateDownloadTask(file);
                    //开启线程下载
                    new Thread(new FileDownloadThread(file)).start();

                }
                //无下载任务，休息
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
    }

    public static void main(String[] args) {
        new Thread(new FileDownloadHandleThread()).start();
    }



}
