package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import model.Product;
import model.User;
import model.cart.CartItem;
import service.ICartService;
import service.IProductService;
import service.impl.CartServiceImpl;
import service.impl.ProductServiceImpl;
import utils.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/update-quantity-cart-item")
public class UpdateQuantityCartItemController extends HttpServlet {
    private ICartService cartService = new CartServiceImpl();
    private IProductService productService = new ProductServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(req.getInputStream(), Map.class);

        System.out.println(data);
        Integer productId = Integer.parseInt(data.get("productId").toString());
        Integer quantity = Integer.parseInt(data.get("quantity").toString());

        User user = (User) SessionUtil.getInstance().getKey(req, "user");
        Integer userId = user != null ? user.getId() : null;
        Integer cartId = userId != null ? cartService.findByUserId(userId).getId() : null;

        boolean isUpdate = false;
        if (productId != null && userId != null && cartId != null) {
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cartId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            isUpdate = cartService.updateCartItem(cartItem);
        }
        System.out.println(isUpdate);

        Product product = productService.findProductById(productId);
        double newPrice = product.getPrice() * quantity;
        JsonObject response = new JsonObject();
        if (isUpdate) {
            resp.setStatus(HttpServletResponse.SC_OK);
            response.addProperty("message", "Cập nhật thành công");
            response.addProperty("newPrice", newPrice);
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.addProperty("message", "Cập nhật thất bại");
        }

        resp.getWriter().print(response);
        resp.flushBuffer();
    }
}
