package com.add.servlet;

import com.add.dao.FileDao;
import com.add.model.File;
import com.add.model.FileFolder;
import com.add.model.UploadFile;
import com.add.util.FileUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mergeUploadFile")
public class MergeUploadFile extends AbstractBaseServlet {

    private FileDao fileDao = new FileDao();

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String fileMd5Value = req.getParameter("fileMd5Value");
        Integer folderId = Integer.parseInt(req.getParameter("folderId"));
        Integer storeId = Integer.parseInt(req.getParameter("storeId"));

        //获取uploadFile
        UploadFile uploadFile = FileUtil.getUploadFile(fileMd5Value);
        FileUtil.removeUploadFile(fileMd5Value);

        //插入数据库
        File myFile = new File();
        myFile.setFileName(uploadFile.getFileName());
        myFile.setFileSize(uploadFile.getFileSize());
        myFile.setFilePostfix(uploadFile.getFileName().substring(uploadFile.getFileName().lastIndexOf(".")));
        myFile.setFileStorehouseId(storeId);
        //获取文件上传的路径
        FileFolder fileFolder = fileDao.queryFileFolderByFileFolderId(folderId);
        if (fileFolder == null) {
            myFile.setParentFolderId(-1);
            myFile.setFilePath(storeId + "/" + myFile.getFileName());
        } else {
            myFile.setParentFolderId(folderId);
            myFile.setFilePath(fileFolder.getFileFolderPath() + "/" + myFile.getFileName());
        }
        System.out.println(myFile);
        //插入数据库
        fileDao.uploadFile(myFile);
        return true;
    }
}
