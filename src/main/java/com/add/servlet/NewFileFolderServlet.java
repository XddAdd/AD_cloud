package com.add.servlet;

import com.add.dao.FileDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/newFileFolder")
public class NewFileFolderServlet extends AbstractBaseServlet{
    private FileDao fileDao = new FileDao();
    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Integer storeId = Integer.parseInt(req.getParameter("storeId"));
        Integer folderId = Integer.parseInt(req.getParameter("folderId"));
        String folderName = req.getParameter("folderName");
        System.out.println(folderName);
        Integer line = fileDao.newFileFolder(storeId,folderId,folderName);
        return line;
    }
}
