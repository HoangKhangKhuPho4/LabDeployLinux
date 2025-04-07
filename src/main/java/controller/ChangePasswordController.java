package controller;

import org.mindrot.jbcrypt.BCrypt;
import utils.SessionUtil;
import utils.TwoFactorAuth;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "ChangePasswordController", value = "/changePassword")
public class ChangePasswordController extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getInstance().getKey(req, "user");
        if (user == null) {
            resp.sendRedirect("dangnhap.jsp");
            return;
        }
        // Nếu 2FA chưa kích hoạt (chưa có secretKey), chuyển sang trang kích hoạt 2FA
        if (user.getSecretKey() == null || user.getSecretKey().trim().isEmpty()) {
            String secretKey = TwoFactorAuth.generateSecretKey();
            user.setSecretKey(secretKey);
            SessionUtil.getInstance().putKey(req, "user", user);
            RequestDispatcher dispatcher = req.getRequestDispatcher("generateQRCode.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        // Nếu 2FA đã kích hoạt thì kiểm tra xem người dùng có tái xác thực OTP chưa
        Boolean isOTPVerified = (Boolean) req.getSession().getAttribute("isOTPVerified");
        if (isOTPVerified == null || !isOTPVerified) {
            // Chuyển đến trang OTP re‑authentication
            RequestDispatcher dispatcher = req.getRequestDispatcher("reAuthOTP.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        // Nếu đã OTP verified, chuyển sang trang đổi mật khẩu
        req.setAttribute("user", user);
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

        // Nếu các trường mật khẩu chưa được nhập, chỉ hiển thị trang đổi mật khẩu mà không thực hiện kiểm tra
        if ((currentPassword == null || currentPassword.trim().isEmpty()) &&
                (newPassword == null || newPassword.trim().isEmpty()) &&
                (confirmPassword == null || confirmPassword.trim().isEmpty())) {
            req.getRequestDispatcher("changePassword.jsp").forward(req, resp);
            return;
        }

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
        user.setUpdatedAt(LocalDateTime.now());
        user.setTwoFaEnabled(true);
        userService.update(user);

        SessionUtil.getInstance().putKey(req, "user", user);

        // Xóa cờ OTP verified sau khi thực hiện giao dịch nhạy cảm
        req.getSession().removeAttribute("isOTPVerified");

        req.setAttribute("success", "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
        req.getRequestDispatcher("changePassword.jsp").forward(req, resp);
    }
}