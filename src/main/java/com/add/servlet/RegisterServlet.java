package com.add.servlet;


import com.add.dao.FileDao;
import com.add.dao.UserDao;
import com.add.exception.AppException;
import com.add.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends AbstractBaseServlet{
    private UserDao userDao = new UserDao();
    private FileDao fileDao = new FileDao();

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //获取注册的信息
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String sex = req.getParameter("sex");
        String nickname = req.getParameter("nickname");
        //判断username是否已经存在
        if (userDao.queryByUsername(username) != null){
            throw new AppException("REG001","用户名已经存在");
        }
        //username合法，注册
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setNickname(nickname);
        //数据库中注册
        userDao.register(user);
        //添加仓库
        fileDao.newFileStorehouse(user.getId());

        resp.sendRedirect("view/login.html");
        return null;
    }
}
