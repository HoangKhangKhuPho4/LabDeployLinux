package controller;

import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/account/edit")
public class EditAccountController extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        Integer id = Integer.parseInt(req.getParameter("id"));
        User user = userService.getById(id);
        objectMapper.writeValue(resp.getOutputStream(), user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            Integer id = Integer.parseInt(req.getParameter("id"));
            User existingUser = userService.getById(id);

            existingUser.setName(req.getParameter("name"));
            existingUser.setEmail(req.getParameter("email"));
            existingUser.setUsername(req.getParameter("user"));
            existingUser.setPhone(req.getParameter("phone"));
            existingUser.setAddress(req.getParameter("address"));

            String birthDateStr = req.getParameter("date");
            if (birthDateStr != null && !birthDateStr.isEmpty()) {
                existingUser.setBirth(LocalDate.parse(birthDateStr, DateTimeFormatter.ISO_DATE));
            }
            existingUser.setGender(req.getParameter("gender"));

            String newPassword = req.getParameter("password");
            if (newPassword != null && !newPassword.isEmpty() && !BCrypt.checkpw(newPassword, existingUser.getPassword())) {
                // Nếu mật khẩu mới được cung cấp và khác với mật khẩu cũ (đã mã hóa), hãy mã hóa nó
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                existingUser.setPassword(hashedPassword);
            }

            // Các trường OAuth (nếu không đổi thì có thể giữ nguyên giá trị cũ)
            existingUser.setOauthProvider(existingUser.getOauthProvider());
            existingUser.setOauthUid(existingUser.getOauthUid());
            existingUser.setOauthToken(existingUser.getOauthToken());

            // roleId và status có thể set giá trị mặc định hoặc giữ nguyên giá trị hiện tại
            existingUser.setRoleId(existingUser.getRoleId());
            existingUser.setStatus(existingUser.getStatus());

            // Cập nhật lại ngày chỉnh sửa
            existingUser.setUpdatedAt(LocalDateTime.now());

            // Kiểm tra trùng lặp email và username (ngoại trừ chính tài khoản này)
            User checkUserEmail = userService.getByUsername(existingUser.getUsername());
            if (checkUserEmail != null && !checkUserEmail.getId().equals(id) && checkUserEmail.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
                resp.sendRedirect("/quanlytaikhoan?error=duplicateEmail&id=" + id);
                return;
            }
            User checkUserUsername = userService.getByUsername(existingUser.getUsername());
            if (checkUserUsername != null && !checkUserUsername.getId().equals(id) && checkUserUsername.getUsername().equalsIgnoreCase(existingUser.getUsername())) {
                resp.sendRedirect("/quanlytaikhoan?error=duplicateUsername&id=" + id);
                return;
            }


            userService.update(existingUser);
            resp.sendRedirect("/quanlytaikhoan?success=edit");

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
