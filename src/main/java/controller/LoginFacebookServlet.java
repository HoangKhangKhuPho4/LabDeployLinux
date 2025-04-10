package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facebook4j.User; // Đây là facebook4j.User
import model.FacebookAccount;
import facebook.RestFB;
import service.impl.UserServiceImpl;
import utils.SessionUtil;

/**
 * Servlet implementation class LoginFacebookServlet
 */
@WebServlet("/login-facebook")
public class LoginFacebookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginFacebookServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            RequestDispatcher dis = request.getRequestDispatcher("accessDenied.jsp");
            dis.forward(request, response);
        }
        else {
            try {
                // Lấy access token từ code (in ra console để debug)
                String accessToken = RestFB.getToken(code);
                System.out.println("Access Token: " + accessToken);

                // Lấy thông tin người dùng từ Facebook theo access token
                User fbUser = RestFB.getUserInfo(accessToken);

                // Tạo đối tượng FacebookAccount để hiển thị tạm (nếu cần)
                FacebookAccount fbAccount = new FacebookAccount();
                fbAccount.setId(fbUser.getId());
                fbAccount.setName(fbUser.getName());
                fbAccount.setEmail(fbUser.getEmail());
                if (fbUser.getPicture() != null) {
                    try {
                        fbAccount.setPicture(fbUser.getPicture().getURL().toString());
                    } catch (Exception e) {
                        fbAccount.setPicture(fbUser.getPicture().toString());
                    }
                } else {
                    fbAccount.setPicture("");
                }

                // Chuyển đổi thông tin từ FacebookAccount sang đối tượng model.User của hệ thống
                // (Để tránh nhầm lẫn giữa facebook4j.User và model.User, ta đặt tên biến là appUser)
                model.User appUser = new model.User();
                // Dùng email làm username nếu có, nếu không thì tạo username theo "fb_" + id
                if (fbAccount.getEmail() != null && !fbAccount.getEmail().isEmpty()) {
                    appUser.setUsername(fbAccount.getEmail());
                } else {
                    appUser.setUsername("fb_" + fbAccount.getId());
                }
                appUser.setPassword(""); // OAuth không cần mật khẩu
                appUser.setOauthProvider("facebook");
                appUser.setOauthUid(fbAccount.getId());
                appUser.setOauthToken(accessToken);
                appUser.setName(fbAccount.getName());
                appUser.setEmail(fbAccount.getEmail());
                appUser.setPicture(fbAccount.getPicture());
                // Giả sử role 4 là role của user bình thường
                appUser.setRoleId(4);
                appUser.setCreatedAt(LocalDateTime.now());
                appUser.setUpdatedAt(LocalDateTime.now());
                appUser.setStatus(1);
                // Các trường 2FA: secretKey rỗng và twoFaEnabled là false
                appUser.setSecretKey(null);
                appUser.setTwoFaEnabled(false);

                // Gọi service để lưu tài khoản Facebook vào DB (nếu chưa có, bạn có thể thêm kiểm tra trước khi lưu)
                UserServiceImpl userService = new UserServiceImpl();
                boolean dbResult = userService.addFacebookUser(appUser);
                if (dbResult) {
                    System.out.println("Facebook user registration successful");
                } else {
                    System.err.println("Facebook user registration failed");
                }

                // Lưu đối tượng user vào session để sử dụng cho các phiên làm việc sau
                SessionUtil.getInstance().putKey(request, "user", appUser);

                // Điều hướng về trang chủ để cập nhật thông tin (header.jsp sẽ hiển thị thông tin từ session)
                response.sendRedirect("index.jsp");
            } catch (Exception e) {
                e.printStackTrace(); // In ra chi tiết lỗi để debug
                throw new ServletException(e);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
