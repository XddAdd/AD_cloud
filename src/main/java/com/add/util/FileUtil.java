package com.add.util;

import com.add.exception.AppException;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {

    public static String ROOT = "D:/myFtp/";
    //public static String ROOT = "/usr/myFTP/";

    /**
     * 新建文件夹
     * @param folderPath
     * @return
     */
    public static Boolean newFileFolder(String folderPath){
        File f = new File(FileUtil.ROOT + folderPath);
        return f.mkdir();
    }

    /**
     * 新建用户文件夹
     * @param userId
     * @return
     */
    public static Boolean newUserFileStore(Integer userId){
        File f = new File(FileUtil.ROOT + userId);
        return f.mkdir();
    }

    /**
     * 判断当前路径是否存在
     * @param path
     * @return
     */
    public static Boolean isFileExisted(String path){
        File f = new File(path);
        return f.exists();
    }

    /**
     * 上传文件到指定路径
     * @param file 文件对象
     * @param uploadPath 路径
     * @return
     */
    public static Boolean uploadFile(FileItem file,String uploadPath){
        try {
            //写入文件到磁盘，该行执行完毕后，若有该临时文件，将会自动删除
            file.write(new File(uploadPath));
            return true;
        } catch (Exception e) {
            throw new AppException("FILE006","文件上传出错");
        }
    }

    /**
     * 根据文件绝对路径，获取相对路径
     * @param filePath
     * @return
     */
    public static String getRelativeFilePath(String filePath){
        filePath = filePath.substring(FileUtil.ROOT.length());
        return filePath;
    }

    /**
     * 根据http下载路径，返回对应的Http连接
     * @param downloadUrlPath
     * @return
     */
    public static HttpURLConnection getHttpURLConnection(String downloadUrlPath) {
        URL downloadUrl = null;
        HttpURLConnection coon = null;
        try {
            downloadUrl = new URL(downloadUrlPath);
            coon = (HttpURLConnection) downloadUrl.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(5000);
            coon.getResponseCode();
            return coon;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException("Download001","请求目标路径或获取链接异常");
        }
    }

    public static Long getFileSizeFromHttpURLConnection(HttpURLConnection connection){
        try{
            int code = connection.getResponseCode();
            if (code == 200){
                return  (long) connection.getContentLength();
            }
            return -1L;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException("Download002","获取下载链接的文件大小出错");
        }

    }






}
