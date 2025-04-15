package controller;

import model.CartResponse;
import model.Log;
import model.User;
import model.cart.Cart;
import service.ICartService;
import service.ILogService;
import service.impl.CartServiceImpl;
import service.impl.LogServiceImpl;
import utils.LevelLog;
import utils.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DisplayCartItemController", value = "/display-cart")
public class DisplayCartItemController extends HttpServlet {
    private ICartService cartService = new CartServiceImpl();
    private ILogService logService = new LogServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        User user = (User) SessionUtil.getInstance().getKey(req, "user");
        if(user == null) {
            resp.sendRedirect("dangnhap.jsp");
            return;
        }
        Integer userId = user.getId();

        Cart cart = cartService.findByUserId(userId);
        Log log = new Log();
        log.setUserId(userId);
        log.setAction("Xem giỏ hàng");
        log.setAddressIP(req.getRemoteAddr());
        log.setLevel(LevelLog.INFO);
        logService.save(log);

        if (cart != null) {
            double totalPrice = 0d;
            List<CartResponse> cartItems = cartService.getCartItemByCartId(cart.getId());
            for (CartResponse cartResponse : cartItems){
                double total = cartResponse.getQuantity() * cartResponse.getProduct().getPrice();
                totalPrice += total;
            }

            req.setAttribute("cartItems", cartItems);
            req.setAttribute("totalPrice", totalPrice);
        }
        req.getRequestDispatcher("giohang.jsp").forward(req, resp);
    }
}
