package com.add.servlet;


import com.add.dao.FileDao;
import com.add.model.FileFolder;
import com.add.model.UploadFile;
import com.add.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@WebServlet("/uploadFile")
public class BigUploadFileServlet extends AbstractBaseServlet {

    private FileDao fileDao = new FileDao();

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //获取磁盘文件工厂
        DiskFileItemFactory factory=new DiskFileItemFactory();
        //设置参数
        factory.setSizeThreshold(1024 * 1024 * 30);//设置缓冲区大小
        factory.setRepository(new File(FileUtil.ROOT));//设置缓冲区文件保存位置
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);//获取上传文件组件
        servletFileUpload.setHeaderEncoding("utf-8");

        //解析表单项
        List<FileItem> fileItems = servletFileUpload.parseRequest(req);
        FileItem file = null;//文件项
        String fileMd5Value = null;
        String fileName = null;
        Long fileSize = null;
        Integer chunks = null;
        Integer chunk = null;
        Integer storeId = null;//文件仓库id
        Integer folderId = null;//文件夹id
        String uploadPath = FileUtil.ROOT;
        for (FileItem fileItem : fileItems){
            //文件字段，文件名不空
            if (!fileItem.isFormField() && fileItem.getName() != null && !"".equals(fileItem.getName())){
                file = fileItem;
            }else{
                //解析非文件字段
                switch (fileItem.getFieldName()) {
                    case "fileMd5Value" : fileMd5Value = fileItem.getString();
                        break;
                    case "fileName" : fileName = fileItem.getString("UTF-8");
                        break;
                    case "fileSize" : fileSize = Long.parseLong(fileItem.getString());
                        break;
                    case "chunks" : chunks = Integer.parseInt(fileItem.getString());
                        break;
                    case "chunk" : chunk = Integer.parseInt(fileItem.getString());
                        break;
                    case "storeId" : storeId = Integer.parseInt(fileItem.getString());
                        break;
                    case "folderId" : folderId = Integer.parseInt(fileItem.getString());
                        break;
                }
            }
        }
        //获取文件上传的路径
        FileFolder fileFolder = fileDao.queryFileFolderByFileFolderId(folderId);
        //判断是否为根目录
        if (fileFolder == null) uploadPath += storeId + "/" + fileName;
        else uploadPath += fileFolder.getFileFolderPath() + "/" + fileName;
        System.out.println(uploadPath);

        //不存在文件，上传文件
        UploadFile uploadFile = FileUtil.getUploadFile(fileMd5Value, fileName, fileSize, chunks);
        FileUtil.uploadChunkFile(uploadPath, fileSize, file.getInputStream(), file.getSize(), chunk, chunks);
        //当前chunk状态置为上传成功
        System.out.println(uploadFile);
        uploadFile.getChunkList().set(chunk, true);
        //文件全部上传完成
        if (chunk == chunks - 1) uploadFile.setFinish(true);
        System.out.println("chunk" + " " + chunk + "成功");

        return true;
    }
}
