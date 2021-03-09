package com.add.thread;

import com.add.dao.FileDao;
import com.add.model.FileInfo;
import com.add.util.FileUtil;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;



public class FileDownloadThread implements Runnable {
    private FileInfo fileInfo;
    private FileDao fileDao = new FileDao();


    public FileDownloadThread(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection conn = FileUtil.getHttpURLConnection(fileInfo.getUrl());

            //获得服务器端目标文件的长度
            Long fileSize = fileInfo.getFileSize();
            System.out.println("服务器端文件的大小是：" + fileSize);

            //1、在本地弄一个和目标文件一样大的file对象
            File descFile = new File(fileInfo.getTargetPath());
            RandomAccessFile raf = new RandomAccessFile(descFile, "rw");
            raf.setLength(fileSize);

            //2、计算出每一个线程应该下载文件的大小

            //3、去下载文件
            InputStream inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);

            //指定文件开始读取的位置
            raf.seek(0);
            byte[] buffer = new byte[1024];
            int len = 0, i = 0;
            Long hasDownloadSize = 0L;
            while ((len = bis.read(buffer)) != -1) {
                hasDownloadSize += len;
                raf.write(buffer, 0, len);
                fileInfo.setDownloadSize(hasDownloadSize);
                i ++;
                if (i == 1e3 || hasDownloadSize == fileSize) {
                    fileDao.updateDownloadTaskDownloadSize(fileInfo);
                    i = 0;
                }
            }
            bis.close();
            raf.close();
            //下载完成
            System.out.println("下载完成");

            //从任务队列中删除该任务，表示下载完成
            fileDao.deleteOneFromDownloadTask(fileInfo);

            //插入文件表
            com.add.model.File file = new com.add.model.File();
            file.setFilePath(FileUtil.getRelativeFilePath(fileInfo.getTargetPath()));
            file.setFileStorehouseId(fileInfo.getFileStorehouseId());
            file.setParentFolderId(fileInfo.getParentFolderId());
            file.setFileName(fileInfo.getTargetPath().substring(fileInfo.getTargetPath().lastIndexOf("/") + 1));
            file.setFilePostfix(file.getFileName().substring(file.getFileName().lastIndexOf(".")));
            file.setFileSize(fileSize);
            fileDao.uploadFile(file);

            inputStream.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
