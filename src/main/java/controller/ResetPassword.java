package controller;

import service.IUserService;
import service.impl.UserServiceImpl;
import utils.SessionUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/resetpassword")
public class ResetPassword extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(SessionUtil.getInstance().getKey(req, "email") != null) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("resetPassword.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        resp.sendRedirect("/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtil.getInstance().getKey(req, "email") == null) {
            resp.sendRedirect("/");
            return;
        }
        String password = req.getParameter("password");
        String enterPassword = req.getParameter("enterPassword");
        if (password.equals(enterPassword)) {
            userService.resetPass(SessionUtil.getInstance().getKey(req, "email").toString(), password);
            SessionUtil.getInstance().delKey(req, "email");
            SessionUtil.getInstance().delKey(req, "codes");
            // Đặt thông báo thành công
            req.setAttribute("success", "Cập nhật mật khẩu thành công! Chuyển hướng đến đăng nhập sau 3 giây.");
            // Forward về cùng trang resetPassword.jsp để hiển thị thông báo
            RequestDispatcher dispatcher = req.getRequestDispatcher("resetPassword.jsp");
            dispatcher.forward(req, resp);
        } else {
            req.setAttribute("error", "Mật khẩu và nhập lại mật khẩu không giống nhau");
            RequestDispatcher dispatcher = req.getRequestDispatcher("resetPassword.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
