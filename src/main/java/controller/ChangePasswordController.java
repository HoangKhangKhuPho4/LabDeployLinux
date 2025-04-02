package controller;

import model.User;
import org.mindrot.jbcrypt.BCrypt;
import service.IUserService;
import service.impl.UserServiceImpl;
import utils.SessionUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet(name = "ChangePasswordController", value = "/changePassword")
public class ChangePasswordController extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward đến trang đổi mật khẩu
        RequestDispatcher dispatcher = req.getRequestDispatcher("changePassword.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getInstance().getKey(req, "user");
        if (user == null) {
            resp.sendRedirect("dangnhap.jsp");
            return;
        }
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            req.setAttribute("error", "Mật khẩu hiện tại không đúng!");
            req.getRequestDispatcher("changePassword.jsp").forward(req, resp);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu mới và xác nhận không khớp!");
            req.getRequestDispatcher("changePassword.jsp").forward(req, resp);
            return;
        }

        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedNewPassword);
        user.setUpdatedAt(java.time.LocalDateTime.now());

        userService.update(user);
        SessionUtil.getInstance().putKey(req, "user", user);

        req.setAttribute("success", "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
        req.getRequestDispatcher("changePassword.jsp").forward(req, resp);
    }
}