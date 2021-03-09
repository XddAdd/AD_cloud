package com.add.dao;

import com.add.exception.AppException;
import com.add.model.*;
import com.add.util.DBUtil;
import com.add.util.FileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileDao {

    /**
     * 通过文件仓库id查询文件仓库
     * @param fileStoreId
     * @return
     */
    public FileStorehouse queryFileStorehouseByFileStoreId(Integer fileStoreId){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询文件仓库
            String sql = "SELECT * FROM file_storehouse WHERE id = ?";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setInt(1,fileStoreId);
            //设置文件仓库
            res = pre.executeQuery();
            FileStorehouse fileStorehouse = new FileStorehouse();
            res.next();
            fileStorehouse.setId(res.getInt("id"));
            fileStorehouse.setUserId(res.getInt("user_id"));
            fileStorehouse.setCurrentSize(res.getLong("current_size"));
            fileStorehouse.setMaxSize(res.getLong("max_size"));
            return fileStorehouse;
        } catch (Exception e){
            throw new AppException("FILE001","查询文件仓库数据库出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 通过文件仓库id和文件夹id查询当前文件夹的子文件和文件夹
     * @param fileStoreId 文件仓库id
     * @param fileFolderId 文件夹id
     * @return
     */
    public FileStorehouse queryFileStorehouseByFileStoreIdFileFolderId(Integer fileStoreId, Integer fileFolderId){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            FileStorehouse fileStorehouse = queryFileStorehouseByFileStoreId(fileStoreId);
            //查询子文件夹列表
            String sql = "SELECT * FROM my_file_folder WHERE file_storehouse_id = ? and parent_folder_id = ?;";
            pre = conn.prepareStatement(sql);
            pre.setInt(1,fileStoreId);
            pre.setInt(2,fileFolderId);
            res = pre.executeQuery();
            List<FileFolder> fileFolderList = new ArrayList<FileFolder>();
            while (res.next()){
                FileFolder f = new FileFolder();
                f.setId(res.getInt("id"));
                f.setParentFolderId(fileFolderId);
                f.setFileStorehouseId(fileStoreId);
                f.setFileFolderName(res.getString("file_folder_name"));
                f.setFileFolderPath(res.getString("file_folder_path"));
                //日期的使用，java一般使用java.util.Date,年月日时分秒
                java.sql.Date createTime = res.getDate("create_time");
                if (createTime != null)
                    f.setCreateTime(new Date(createTime.getTime()));
                fileFolderList.add(f);
            }
            //查询子文件列表
            sql = "SELECT * FROM my_file WHERE file_storehouse_id = ? and parent_folder_id = ?;";
            pre = conn.prepareStatement(sql);
            pre.setInt(1,fileStoreId);
            pre.setInt(2,fileFolderId);
            res = pre.executeQuery();
            List<File> fileList = new ArrayList<File>();
            while (res.next()){
                File f = new File();
                f.setId(res.getInt("id"));
                f.setParentFolderId(fileFolderId);
                f.setFileStorehouseId(fileStoreId);
                f.setFileName(res.getString("file_name"));
                f.setFilePath(res.getString("file_path"));
                f.setDownloadCount(res.getInt("download_count"));
                f.setFileSize(res.getLong("file_size"));
                f.setFilePostfix(res.getString("file_postfix"));
                //日期的使用，java一般使用java.util.Date,年月日时分秒
                java.sql.Date uploadTime = res.getDate("upload_time");
                if (uploadTime != null)
                    f.setUploadTime(new Date(uploadTime.getTime()));
                fileList.add(f);
            }
            //设置文件仓库列表的子文件和子文件夹列表
            fileStorehouse.setFileFolderList(fileFolderList);
            fileStorehouse.setFileList(fileList);
            return fileStorehouse;
        } catch (Exception e){
            throw new AppException("FILE002","查询文件夹数据库出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 根据文件夹id返回父文件夹id
     * @param storeId 文件仓库id
     * @param folderId 当前文件夹id
     * @return 父文件夹id
     */
    public Integer queryParentFolderIdByCurrentFolderId(Integer storeId, Integer folderId) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询文件仓库
            String sql = "SELECT * FROM my_file_folder WHERE id = ?";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setInt(1,folderId);
            //查询当前文件夹的父文件夹id
            res = pre.executeQuery();
            res.next();
            Integer parentFolderId =res.getInt("parent_folder_id");
            return parentFolderId;
        } catch (Exception e){
            throw new AppException("FILE003","查询当前文件夹父文件夹id数据库出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 下载文件的次数+1
     * @param fileId 下载文件id
     */
    public void addDownloadCount(Integer fileId) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询文件仓库
            String sql = "UPDATE my_file SET download_count = download_count + 1 WHERE id = ?";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setInt(1,fileId);
            //执行sql
            pre.executeUpdate();
        } catch (Exception e){
            throw new AppException("FILE004","增加文件下载次数数据库错误",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     *
     * 新建文件夹
     * @param storeId 文件仓库id
     * @param parentFolderId 父文件夹id
     * @param folderName 文件名
     * @return 影响行数
     */
    public Integer newFileFolder(Integer storeId, Integer parentFolderId, String folderName) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询父文件夹对象
            FileFolder parentFolder = queryFileFolderByFileFolderId(parentFolderId);
            //数据库新建文件夹
            String sql = "INSERT INTO my_file_folder(file_folder_name,parent_folder_id,file_folder_path,file_storehouse_id)" +
                    "VALUES(?,?,?,?);";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setString(1,folderName);
            pre.setInt(2,parentFolderId);
            String path = null;
            if (parentFolder != null) path = parentFolder.getFileFolderPath() + "/" + folderName;
            else path = storeId + "/" + folderName;
             pre.setString(3, path);
            pre.setInt(4,storeId);
            //执行sql
            Integer line = pre.executeUpdate();
            //去本地磁盘创建文件
            System.out.println(path);
            Boolean isSuccess = FileUtil.newFileFolder(path);
            System.out.println(isSuccess);
            return line;
        } catch (Exception e){
            throw new AppException("FILE006","新建文件数据库错误",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 根据文件夹id查询文件夹对象
     * @param fileFolderId 文件夹id
     * @return 文件夹对象
     */
    public FileFolder queryFileFolderByFileFolderId(Integer fileFolderId){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询文件仓库
            String sql = "select * from my_file_folder where id = ?";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setInt(1,fileFolderId);
            //执行sql
            res = pre.executeQuery();
            FileFolder fileFolder = null;
            if (res.next()){
                fileFolder = new FileFolder();
                fileFolder.setId(res.getInt("id"));
                fileFolder.setFileFolderName(res.getString("file_folder_name"));
                fileFolder.setParentFolderId(res.getInt("parent_folder_id"));
                fileFolder.setFileFolderPath(res.getString("file_folder_path"));
                fileFolder.setFileStorehouseId(res.getInt("file_storehouse_id"));
                fileFolder.setCreateTime(res.getDate("create_time"));
            }
            return fileFolder;
        } catch (Exception e){
            throw new AppException("FILE005","查询文件夹对象数据库错误",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 新建文件仓库
     * @param id 文件仓库id = userId
     */
    public void newFileStorehouse(Integer id) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询文件仓库
            String sql = "insert into file_storehouse(user_id)" +
                    "values(?);";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setInt(1,id);
            //查询当前文件夹的父文件夹id
            pre.executeUpdate();
        } catch (Exception e){
            throw new AppException("FILE004","新建文件仓库数据库出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }


    /**
     * 新建文件信息
     * @param file
     */
    public void uploadFile(File file) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询文件仓库
            String sql = "INSERT INTO my_file(file_name, file_path, parent_folder_id, file_size, file_postfix, file_storehouse_id)" +
                    "VALUES (?, ?, ?, ?, ?, ?);";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setString(1,file.getFileName());
            pre.setString(2,file.getFilePath());
            pre.setInt(3,file.getParentFolderId());
            pre.setLong(4,file.getFileSize());
            pre.setString(5,file.getFilePostfix());
            pre.setInt(6,file.getFileStorehouseId());
            //数据库插入文件信息
            pre.executeUpdate();
        } catch (Exception e){
            throw new AppException("FILE005","新建文件信息数据库出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }


    /**
     * 插入下载任务信息
     * @param fileInfo
     */
    public void insertDownloadTaskFromHttp(FileInfo fileInfo){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //插入数据库下载任务
            String sql = "INSERT INTO file_download_task(file_storehouse_id,parent_folder_id,download_url, target_path, file_size)" +
                    "VALUES (?,?,?,?,?);";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setInt(1,fileInfo.getFileStorehouseId());
            pre.setInt(2,fileInfo.getParentFolderId());
            pre.setString(3,fileInfo.getUrl());
            pre.setString(4,fileInfo.getTargetPath());
            pre.setLong(5,fileInfo.getFileSize());
            //数据库插入文件信息
            pre.executeUpdate();
        } catch (Exception e){
            throw new AppException("TASK008","插入下载任务到数据库出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 判断任务队列是否已经有相同的任务
     * @param fileInfo
     * @return
     */
    public Boolean isDownloading(FileInfo fileInfo){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //插入数据库下载任务
            String sql = "select * from file_download_task where download_url = ? and target_path = ? and download_status = ?;";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setString(1,fileInfo.getUrl());
            pre.setString(2,fileInfo.getTargetPath());
            pre.setInt(3, 1);
            //执行查询
            res = pre.executeQuery();
            return res.next();
        } catch (Exception e){
            throw new AppException("TASK001","判断下载任务队列是否存在该任务",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 从下载任务中查询一条任务，没有任务返回null
     * @return
     */
    public FileInfo queryOneDownloadTask(){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询数据库下载任务
            String sql = "SELECT * FROM file_download_task WHERE download_status = 0 LIMIT 0 , 1;";
            pre = conn.prepareStatement(sql);
            //执行sql
            res = pre.executeQuery();
            //有需要下载的任务
            if (res.next()){
                FileInfo file = new FileInfo();
                file.setFileStorehouseId(res.getInt("file_storehouse_id"));
                file.setParentFolderId(res.getInt("parent_folder_id"));
                file.setUrl(res.getString("download_url"));
                file.setFileSize(res.getLong("file_size"));
                file.setDownloadSize(res.getLong("download_size"));
                file.setId(res.getInt("id"));
                file.setDownloadStatus(res.getInt("download_status"));
                file.setTargetPath(res.getString("target_path"));
                Timestamp start_time = res.getTimestamp("start_time");
                if (start_time != null)
                    file.setStartTime(new Date(start_time.getTime()));

                return file;
            }
            return null;
        } catch (Exception e){
            throw new AppException("TASK002","从数据库取下载任务出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }


    /**
     * 修改任务列表的任务状态
     * @param fileInfo
     * @return 返回影响行数
     */
    public Integer updateDownloadTask(FileInfo fileInfo) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询数据库下载任务
            String sql = "UPDATE file_download_task SET download_status = ? WHERE id = ?;";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, fileInfo.getDownloadStatus());
            pre.setInt(2, fileInfo.getId());
            //执行sql
            int line = pre.executeUpdate();
            //有需要下载的任务
            return line;
        } catch (Exception e){
            throw new AppException("TASK003","数据库修改下载任务表出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 删除一条任务
     * @param fileInfo
     * @return 影响的行数
     */
    public Integer deleteOneFromDownloadTask(FileInfo fileInfo) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //删除任务
            String sql = "DELETE FROM file_download_task WHERE id = ?;";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, fileInfo.getId());
            //执行sql
            int line = pre.executeUpdate();
            return line;
        } catch (Exception e){
            throw new AppException("TASK004","数据库删除任务出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }


    /**
     * 根据仓库id返回下载任务所有任务
     * @param storeId
     * @return
     */
    public ArrayList<FileInfo> queryAllDownloadTask(Integer storeId) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //删除任务
            String sql = "SELECT * FROM file_download_task WHERE file_storehouse_id = ?;";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, storeId);
            //执行sql
            res = pre.executeQuery();
            ArrayList<FileInfo> fileInfos = new ArrayList<>();
            while (res.next()){
                FileInfo fileInfo = new FileInfo();
                fileInfo.setId(res.getInt("id"));
                fileInfo.setParentFolderId(res.getInt("parent_folder_id"));
                fileInfo.setFileStorehouseId(res.getInt("file_storehouse_id"));
                fileInfo.setUrl(res.getString("download_url"));
                fileInfo.setTargetPath(res.getString("target_path"));
                fileInfo.setFileSize(res.getLong("file_size"));
                fileInfo.setDownloadSize(res.getLong("download_size"));
                Timestamp start_time = res.getTimestamp("start_time");
                if (start_time != null)
                    fileInfo.setStartTime(new Date(start_time.getTime()));
                fileInfo.setDownloadStatus(res.getInt("download_status"));
                fileInfos.add(fileInfo);
            }
            return fileInfos;
        } catch (Exception e){
            throw new AppException("TASK005","数据库返回所属id的下载任务出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    public Integer updateDownloadTaskDownloadSize(FileInfo fileInfo) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            //查询数据库下载任务
            String sql = "UPDATE file_download_task SET download_size = ? WHERE id = ?;";
            pre = conn.prepareStatement(sql);
            pre.setLong(1, fileInfo.getDownloadSize());
            pre.setInt(2, fileInfo.getId());
            //执行sql
            int line = pre.executeUpdate();
            //有需要下载的任务
            return line;
        } catch (Exception e){
            throw new AppException("TASK004","数据库修改下载任务已经下载大小出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }


}
