package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

@WebServlet(value = "/account/delete")
public class DeleteAccountController extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        try {
            Integer id = Integer.parseInt(idParam);
            userService.deleteById(id);
            resp.sendRedirect("/quanlytaikhoan?success=delete"); // Redirect với thông báo thành công
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("/quanlytaikhoan?error=invalidId"); // Redirect với thông báo lỗi ID
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/quanlytaikhoan?error=deleteFailed"); // Redirect với thông báo lỗi chung
        }
    }
}
