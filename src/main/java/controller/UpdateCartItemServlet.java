package controller;

import com.google.gson.Gson;
import model.cart.Cart;
import model.cart.CartItem;
import model.Product;
import service.ICartService;
import service.impl.CartServiceImpl;
import service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/home/update-quantity-cart-item")
public class UpdateCartItemServlet extends HttpServlet {

    private ICartService cartService = new CartServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Gson gson = new Gson();
        Map<String, String> data = gson.fromJson(sb.toString(), Map.class);

        Integer productId = Integer.parseInt(data.get("productId"));
        Integer quantity = Integer.parseInt(data.get("quantity"));

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Chưa đăng nhập");
            return;
        }

        // Tìm giỏ hàng
        Cart cart = cartService.findByUserId(userId);
        if (cart == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không tìm thấy giỏ hàng");
            return;
        }


        CartItem cartItem = new CartItem();
        cartItem.setCartId(cart.getId());
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);

        boolean success = cartService.updateCartItem(cartItem);


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();

        if (success) {
            jsonResponse.put("status", "success");
        } else {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Cập nhật thất bại");
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
