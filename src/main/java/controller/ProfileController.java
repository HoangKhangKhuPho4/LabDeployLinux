package controller;

import model.User;
import service.IUserService;
import service.impl.UserServiceImpl;
import utils.SessionUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ProfileController", value = "/profile")
public class ProfileController extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Option 1: Retrieve the user from the session, assuming it's already stored
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Redirect to login if no user is found
            resp.sendRedirect("dangnhap.jsp");
            return;
        }

        req.setAttribute("user", user);
        RequestDispatcher dispatcher = req.getRequestDispatcher("profile.jsp");
        dispatcher.forward(req, resp);
    }
}