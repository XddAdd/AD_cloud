package com.add.util;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    //连接数据库信息
    private static final String URL = "jdbc:mysql://localhost:3306/cloud";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    //工具类提供JDBC操作
    private static final DataSource DS = new MysqlDataSource();

    /**
     * 工具类提供数据库JDBC操作
     * 不足：1.static代码块出现错误，NoClassDefFoundError表示类可以找到，但是类加载失败，无法运行
     * 2.多线程中，可能会采取双重校验锁的单例模式来创建DataSource
     * 3.工具类设计上不是最优的,数据库框架ORM框架Mybatis，都是采用模板模式设计的
     */
    static {
        ((MysqlDataSource) DS).setURL(URL);
        ((MysqlDataSource) DS).setUser(USERNAME);
        ((MysqlDataSource) DS).setPassword(PASSWORD);
    }

    /**
     * 获取连接池中的连接对象
     * @return 连接池中的连接对象
     */
    public static Connection getConnection(){
        try {
            return DS.getConnection();
        } catch (SQLException e) {
            //抛自定义异常
            throw new RuntimeException("获取连接对象失败",e);
        }
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        try{
            if (resultSet !=null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException e){
            throw new RuntimeException("关闭资源失败",e);
        }

    }

    public static void close(Connection connection, Statement statement){
        close(connection, statement, null);
    }

}
