package com.add.servlet;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.add.dao.FileDao;
import com.add.exception.AppException;
import com.add.model.FileFolder;
import com.add.model.UploadFile;
import com.add.util.FileUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/fileExist")
public class FileExistServlet extends AbstractBaseServlet {
    private static Map<String, UploadFile> fileMap = FileUtil.fileMap;
    private FileDao fileDao = new FileDao();

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String fileName = req.getParameter("fileName");
        String fileMd5Value = req.getParameter("fileMd5Value");
        Integer folderId = Integer.parseInt(req.getParameter("folderId"));
        Integer storeId = Integer.parseInt(req.getParameter("storeId"));
        UploadFile uploadFile = fileMap.get(fileMd5Value);

        //获取文件上传的路径
        FileFolder fileFolder = fileDao.queryFileFolderByFileFolderId(folderId);
        String uploadPath = FileUtil.ROOT;
        //判断是否为根目录
        if (fileFolder == null) uploadPath += storeId + "/" + fileName;
        else uploadPath += fileFolder.getFileFolderPath() + "/" + fileName;
        //文件已经存在
        if (FileUtil.isFileExisted(uploadPath) && !FileUtil.fileMap.containsKey(fileMd5Value))
            throw new AppException("FILE001","文件已经存在");

        JSONObject res = JSONUtil.createObj();
        if (uploadFile == null) {
            List<Boolean> chunkList = new ArrayList<>();
            return res.put("finish", false).put("chunkList", chunkList);
        }

        return res.put("finish", uploadFile.getFinish()).put("chunkList", uploadFile.getChunkList());

    }
}
