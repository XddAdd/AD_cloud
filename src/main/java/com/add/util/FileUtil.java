package com.add.util;

import com.add.exception.AppException;
import com.add.model.UploadFile;
import org.apache.commons.fileupload.FileItem;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

    public static Map<String, UploadFile> fileMap = new HashMap<>();

    public static String ROOT = "D:/myFtp/";
    //public static String ROOT = "/usr/myFTP/";

    public static Boolean isExist(String md5) {
        return fileMap.containsKey(md5);
    }


    /**
     * 获取uploadFile对象,没有则创建
     */
    public static UploadFile getUploadFile(String md5, String fileName, Long fileSize, Integer chunks) {
        if (!isExist(md5)) {
            synchronized (FileUtil.class) {
                if (!isExist(md5)) {
                    fileMap.put(md5, new UploadFile(md5, fileName, fileSize, chunks));
                }
            }
        }
        return fileMap.get(md5);
    }

    /**
     * 获取uploadFile对象
     */
    public static UploadFile getUploadFile(String md5) {
        return fileMap.get(md5);
    }



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
    public static Boolean uploadAllFile(FileItem file,String uploadPath){
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


    public static void uploadChunkFile(String targetPath, Long targetFileSize, InputStream srcInput, long srcSize, Integer chunk, Integer chunks) throws IOException {
        RandomAccessFile random = new RandomAccessFile(targetPath, "rw");
        random.setLength(targetFileSize);
        if (chunk == chunks) {
            random.seek(targetFileSize - srcSize);
        } else {
            random.seek(chunk * srcSize);
        }
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = srcInput.read(buf))) {
            random.write(buf,0,len);
        }
        random.close();
    }

    public static void removeUploadFile(String fileMd5Value) {
        fileMap.remove(fileMd5Value);
    }
}
