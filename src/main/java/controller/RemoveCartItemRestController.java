package controller;

import model.Log;
import model.Product;
import model.User;
import service.ICartService;
import service.ILogService;
import service.IProductService;
import service.impl.CartServiceImpl;
import service.impl.LogServiceImpl;
import service.impl.ProductServiceImpl;
import utils.LevelLog;
import utils.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveCartItem", value = "/api/remove-cart-item")
public class RemoveCartItemRestController extends HttpServlet {
    private ICartService cartService = new CartServiceImpl();
    private IProductService productService = new ProductServiceImpl();
    private ILogService logService = new LogServiceImpl();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Integer productId = Integer.parseInt(req.getParameter("productId"));
        User user = (User) SessionUtil.getInstance().getKey(req, "user");
        Integer userId = user.getId();
        Integer cartId = cartService.findByUserId(userId).getId();
        Product product = productService.findProductById(productId);
        boolean isRemoved = false;
        if (productId != null && userId != null && cartId != null) {
            isRemoved = cartService.removeCartItem(productId, cartId);  
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if (isRemoved) {
            Log log = new Log();
            log.setUserId(userId);
            log.setAction("Xóa sản phẩm " + product.getName() +" khỏi giỏ hàng");
            log.setAddressIP(req.getRemoteAddr());
            log.setLevel(LevelLog.INFO);
            logService.save(log);
            resp.getWriter().write("{\"status\": \"success\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"status\": \"error\"}");
        }
    }
}
