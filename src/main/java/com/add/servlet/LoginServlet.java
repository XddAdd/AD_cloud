package com.add.servlet;

import com.add.dao.FileDao;
import com.add.dao.UserDao;
import com.add.exception.AppException;
import com.add.model.FileStorehouse;
import com.add.model.User;



import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends AbstractBaseServlet{
    private UserDao userDao = new UserDao();
    private FileDao fileDao = new FileDao();

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //接收请求数据
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //查询数据库并校验
        User user = userDao.queryByUsername(username);
        //验证密码是否正确
        if (user == null) {
            throw new AppException("LOG002","用户名不存在");
        } else if (!user.getPassword().equals(password)) {
            throw new AppException("LOG001","用户名或密码错误");
        }

        //登录成功，创建session
        user.setFileFolderId(-1);;//默认登录后在根路径
        HttpSession session = req.getSession(true);
        session.setAttribute("user",user);
        resp.sendRedirect("view/file_list.html?userId="+user.getId()+"&storeId=" + user.getFileStorehouseId() + "&folderId=-1");
        return user;

    }
}
