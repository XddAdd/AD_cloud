package com.add.servlet;


import com.add.dao.FileDao;
import com.add.model.File;
import com.add.model.FileFolder;
import com.add.model.FileInfo;
import com.add.model.FileStorehouse;
import com.add.util.FileUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@WebServlet("/downloadFileFromHttpServlet")
public class DownloadFileFromHttpServlet extends AbstractBaseServlet{
    private FileDao fileDao = new FileDao();

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //获取下载文件夹id、文件仓库id、下载链接URL
        Integer storeId = Integer.parseInt(req.getParameter("storeId"));
        Integer folderId = Integer.parseInt(req.getParameter("folderId"));
        String downloadURL = req.getParameter("downloadURL");
        String[] split = downloadURL.split("/");
        String fileName = split[split.length - 1];
        System.out.println(fileName);

        //判断目标路径是否已经存在该文件
        Boolean existed = false, isDownloading = false;
        FileStorehouse fileStorehouse = fileDao.queryFileStorehouseByFileStoreIdFileFolderId(storeId, folderId);
        for (File file : fileStorehouse.getFileList()) if (fileName.equals(file.getFileName())) existed = true;
        if (existed) return null;

        //不存在次文件，插入下载任务
        FileInfo fileInfo = new FileInfo();
        String targetPath = null;
        if (folderId == -1) {
            targetPath = FileUtil.ROOT + storeId + "/" + fileName;
            fileInfo.setParentFolderId(-1);
        }
        else {
            targetPath = FileUtil.ROOT + fileDao.queryFileFolderByFileFolderId(folderId).getFileFolderPath() + "/" + fileName;
            fileInfo.setParentFolderId(folderId);
        }
        fileInfo.setFileStorehouseId(storeId);
        fileInfo.setUrl(downloadURL);
        fileInfo.setTargetPath(targetPath);
        fileInfo.setFileSize(FileUtil.getFileSizeFromHttpURLConnection(FileUtil.getHttpURLConnection(downloadURL)));
        fileInfo.setDownloadSize(0L);
        fileInfo.setStartTime(new Date());
        //查询下载任务是否已经存在该下载任务(下载链接和目标路径一样)
        if (fileDao.isDownloading(fileInfo)) fileInfo.setDownloadStatus(1);//表示任务正在下载

        //没有已经存在的任务，添加任务到数据库
        fileDao.insertDownloadTaskFromHttp(fileInfo);
        fileInfo.setDownloadStatus(0);//表示任务插入成功，等待下载
        return fileInfo;
    }
}
