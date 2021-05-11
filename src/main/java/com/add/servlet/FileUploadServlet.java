package com.add.servlet;

import com.add.dao.FileDao;
import com.add.exception.AppException;
import com.add.model.FileFolder;
import com.add.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.List;

@WebServlet("/fileUpload")
public class FileUploadServlet extends AbstractBaseServlet{
    private FileDao fileDao = new FileDao();

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //乱码处理
        //String requestStr = new String(req.toString().getBytes("iso-8859-1","utf-8"));
        Boolean uploadSuccess = false;
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
        String storeId = null;//文件仓库id
        String folderId = null;//文件夹id
        String uploadPath = FileUtil.ROOT;
        for (FileItem fileItem : fileItems){
            //文件字段，文件名不空
            if (!fileItem.isFormField() && fileItem.getName() != null && !"".equals(fileItem.getName())){
                file = fileItem;
            }else{
                //解析非文件字段
                if (fileItem.getFieldName().equals("storeId")) storeId = fileItem.getString();
                else if (fileItem.getFieldName().equals("folderId")) folderId = fileItem.getString();
            }
        }
        //获取文件上传的路径
        FileFolder fileFolder = fileDao.queryFileFolderByFileFolderId(Integer.parseInt(folderId));
        //判断是否为根目录
        if (fileFolder == null) uploadPath += storeId + "/" + file.getName();
        else uploadPath += fileFolder.getFileFolderPath() + "/" + file.getName();
        System.out.println(uploadPath);

        //判断已经存在文件
        if (FileUtil.isFileExisted(uploadPath)) throw new AppException("FILE001","文件已经存在");
        //不存在文件，上传文件
        uploadSuccess = FileUtil.uploadFile(file, uploadPath);


        //上传失败，不插入数据库
        if (!uploadSuccess) return false;
        //上传成功，插入数据库
        com.add.model.File myFile = new com.add.model.File();
        myFile.setFileName(file.getName());
        myFile.setFileSize(file.getSize());
        myFile.setFilePostfix(file.getName().substring(file.getName().lastIndexOf(".")));
        myFile.setFileStorehouseId(Integer.parseInt(storeId));
        if (fileFolder == null) {
            myFile.setParentFolderId(-1);
            myFile.setFilePath(storeId + "/" + myFile.getFileName());
        } else {
            myFile.setParentFolderId(Integer.parseInt(folderId));
            myFile.setFilePath(fileFolder.getFileFolderPath() + "/" + myFile.getFileName());
        }

        fileDao.uploadFile(myFile);

        return true;
    }
}
