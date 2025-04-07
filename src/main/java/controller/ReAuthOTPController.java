package controller;

import model.User;
import utils.SessionUtil;
import utils.TwoFactorAuth;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ReAuthOTPController", value = "/reAuthOTP")
public class ReAuthOTPController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getInstance().getKey(req, "user");
        if (user == null) {
            resp.sendRedirect("dangnhap.jsp");
            return;
        }
        String otpCode = req.getParameter("otpCode");
        if (otpCode == null || otpCode.trim().isEmpty() || !TwoFactorAuth.verifyOTP(user.getSecretKey(), otpCode)) {
            req.setAttribute("error", "OTP không hợp lệ. Vui lòng kiểm tra lại.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("reAuthOTP.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        // Nếu OTP đúng, gán cờ OTP verified và chuyển đến trang đổi mật khẩu
        req.getSession().setAttribute("isOTPVerified", true);
        resp.sendRedirect("changePassword");
    }
}