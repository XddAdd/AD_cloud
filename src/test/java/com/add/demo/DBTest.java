package com.add.demo;

import com.add.exception.AppException;
import com.add.model.User;
import com.add.util.DBUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class DBTest {

    @Test
    public void test1(){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "show tables;";
            //查询并处理结果集
            pre = conn.prepareStatement(sql);
            res = pre.executeQuery();
            while (res.next()){
                String line = res.getString(1);
                System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
                DBUtil.close(conn,pre,res);
        }
    }
}
