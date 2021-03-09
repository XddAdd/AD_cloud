package com.add.dao;

import com.add.exception.AppException;
import com.add.model.User;
import com.add.util.DBUtil;
import com.add.util.FileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class UserDao {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public User queryByUsername(String username){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM user WHERE username = ?;";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setString(1,username);
            //查询并处理结果集
            res = pre.executeQuery();
            User user = null;
            while (res.next()){
                user = new User();
                user.setId(res.getInt("id"));
                user.setUsername(username);
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
            return user;
        } catch (Exception e){
            throw new AppException("LOG001","查询用户操作出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 根据用户名获取用户id
     * @param username 用户名
     * @return 数据库中是否已经存在
     */
    public Integer hasExistedUsername(String username){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM user WHERE username = ?";
            pre = conn.prepareStatement(sql);
            //设置占位符
            pre.setString(1,username);
            //查询并处理结果集
            res = pre.executeQuery();
            if (res.next()) return res.getInt("id");
            return null;
        } catch (Exception e){
            throw new AppException("LOG002","根据用户名查询用户id出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }

    /**
     * 新增用户
     * @param user 用户
     * @return
     */
    public Boolean register(User user){
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO user(username,PASSWORD,nickname,sex)" +
                    "VALUES(?,?,?,?)";
            pre = conn.prepareStatement(sql);
            //设置占位
            pre.setString(1,user.getUsername());
            pre.setString(2,user.getPassword());
            pre.setString(3,user.getNickname());
            pre.setString(4,user.getSex());
            //查询并处理结果集
            int line = pre.executeUpdate();
            if (line == 0) throw new AppException("REG002","注册用户出错");
            Integer userId = hasExistedUsername(user.getUsername());
            sql = "update user set file_storehouse_id = ? where id = ?";
            pre = conn.prepareStatement(sql);
            pre.setInt(1,userId);
            pre.setInt(2,userId);
            pre.executeUpdate();
            user.setId(userId);
            //新建用户文件仓库文件夹
            FileUtil.newUserFileStore(userId);
            return true;
        } catch (Exception e){
            throw new AppException("REG002","根据用户名查询用户id出错",e);
        }finally {
            DBUtil.close(conn,pre,res);
        }
    }
}
