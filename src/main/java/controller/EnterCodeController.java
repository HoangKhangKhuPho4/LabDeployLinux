package controller;

import utils.SessionUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/entercode")
public class EnterCodeController extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("enterCode.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Nếu có email trong session -> reset password
        if (SessionUtil.getInstance().getKey(req, "email") != null) {
            String code = SessionUtil.getInstance().getKey(req, "codes").toString();
            if (code.equals(req.getParameter("code"))) {
                SessionUtil.getInstance().delKey(req, "codes");
                resp.sendRedirect("resetpassword");
                return;
            }
            req.setAttribute("error", "Mã code không chính xác");
            RequestDispatcher dispatcher = req.getRequestDispatcher("enterCode.jsp");
            dispatcher.forward(req, resp);
            return;
        } else {
            // Trường hợp đăng ký
            if (SessionUtil.getInstance().getKey(req, "codes") != null) {
                String code = SessionUtil.getInstance().getKey(req, "codes").toString();
                if (code.equals(req.getParameter("code"))) {
                    if (SessionUtil.getInstance().getKey(req, "user") != null) {
                        User user = (User) SessionUtil.getInstance().getKey(req, "user");
                        String rsRegister = userService.register(user);
                        if (rsRegister == null) {
                            req.setAttribute("error", "Đăng ký thất bại!");
                            RequestDispatcher dispatcher = req.getRequestDispatcher("enterCode.jsp");
                            dispatcher.forward(req, resp);
                            return;
                        } else {
                            // Đặt flag xác nhận OTP thành công trong session
                            SessionUtil.getInstance().putKey(req, "otpSuccess", true);
                            // Xóa các session attribute không cần nữa
                            SessionUtil.getInstance().delKey(req, "codes");
                            SessionUtil.getInstance().delKey(req, "user");
                            RequestDispatcher dispatcher = req.getRequestDispatcher("enterCode.jsp");
                            dispatcher.forward(req, resp);
                            return;
                        }
                    } else {
                        resp.sendRedirect("dangnhap.jsp");
                        return;
                    }
                } else {
                    req.setAttribute("error", "Mã code không chính xác");
                    RequestDispatcher dispatcher = req.getRequestDispatcher("enterCode.jsp");
                    dispatcher.forward(req, resp);
                    return;
                }
            } else {
                resp.sendRedirect("dangnhap.jsp");
                return;
            }
        }
    }
}