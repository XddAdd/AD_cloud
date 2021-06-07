package com.add.servlet;

import com.add.dao.FileDao;
import com.add.model.FileStorehouse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/fileList")
public class FileListServlet extends AbstractBaseServlet{
    private FileDao fileDao = new FileDao();

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Integer storeId = Integer.parseInt(req.getParameter("storeId"));
        Integer folderId = Integer.parseInt(req.getParameter("folderId"));
        FileStorehouse fileStorehouse = fileDao.queryFileStorehouseByFileStoreIdFileFolderId(storeId, folderId);
        System.out.println(fileStorehouse);
        return fileStorehouse;
    }
}
