package com.add.dao;

import com.add.util.DBUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class FileDownloadDao {

    public boolean download(String url){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO file_queue(url,file_status) " +
                    "VALUES(?,0)";
            //设置占位符
            pre = conn.prepareStatement(sql);
            pre.setString(1, url);
            //执行sql
            int line = pre.executeUpdate();
            if (line == 1) return true;
            else return false;

        } catch (Exception e){
            throw new RuntimeException("数据库插入失败",e);
        } finally {
            DBUtil.close(conn,pre,res);
        }
    }


}
