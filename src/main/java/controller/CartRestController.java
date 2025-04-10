package controller;

import com.google.gson.Gson;
import model.User;
import service.ICartService;
import service.impl.CartServiceImpl;
import utils.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Cart", value = "/api/cart/total-cart-item")
public class CartRestController extends HttpServlet {
    private ICartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getInstance().getKey(req, "user");
        if (user == null) {
                resp.sendRedirect("header.jsp");
            return;
        }

        Integer userId = user.getId();
        Integer cartId = cartService.findByUserId(userId).getId();
        Integer totalItem = cartService.getTotalCartItem(cartId);
        if (totalItem == null) {
            totalItem = 0;
        }

        String json = new Gson().toJson(totalItem);
        resp.getWriter().write(json);
    }
}
