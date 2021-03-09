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
            String sql = "SELECT * FROM user WHERE username = ?;";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setString(1, "add");
            //查询并处理结果集
            res = pre.executeQuery();
            User user = null;
            while (res.next()){
                user = new User();
                user.setId(res.getInt("id"));
                user.setUsername("add");
                user.setPassword(res.getString("password"));
                user.setNickname(res.getString("nickname"));
                user.setSex(res.getString("sex"));
                user.setFileStorehouseId(res.getInt("file_storehouse_id"));
                //日期的使用，java一般使用java.util.Date,年月日时分秒
                java.sql.Date birthday = res.getDate("birthday");
                if (birthday != null)
                    user.setBirthday(new Date(birthday.getTime()));
                Date registerTime = res.getTime("register_time");
                if (registerTime != null)
                    user.setRegisterTime(registerTime);
                user.setImagePath(res.getString("image_path"));
            }
            System.out.println(user);
        }catch (Exception e){
            System.out.println(e);
        }finally {
                DBUtil.close(conn,pre,res);
        }
    }
}
