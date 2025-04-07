package controller;

import utils.SessionUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("dangnhap.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        req.setAttribute("username", username);
        req.setAttribute("password", "");

        if (userService.login(username, password)) {
            User loggedInUser = userService.getByUsername(username);

            if (loggedInUser != null) {
                SessionUtil.getInstance().putKey(req, "user", loggedInUser);
                // Sau khi đăng nhập thành công, chuyển hướng theo role
                if (loggedInUser.getRoleId() == 1) { // Admin
                    resp.sendRedirect("admin.jsp");
                } else { // Regular User
                    resp.sendRedirect("index.jsp");
                }
            } else {
                req.setAttribute("error", "Không tìm thấy thông tin người dùng!");
                req.getRequestDispatcher("dangnhap.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", "Tên người dùng hoặc mật khẩu không chính xác!");
            RequestDispatcher dispatcher = req.getRequestDispatcher("dangnhap.jsp");
            dispatcher.forward(req, resp);
        }
    }
}