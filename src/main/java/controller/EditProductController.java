package controller;

import dao.impl.ProductDAOImpl;
import model.Product;
import model.ProductType;
import model.Producer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "EditProductController", urlPatterns = {"/edit"})
public class EditProductController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int productTypeId = Integer.parseInt(request.getParameter("productType"));
            int producerId = Integer.parseInt(request.getParameter("productCategory"));
            String detail = request.getParameter("detail");

            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setDetail(detail);

            ProductType productType = new ProductType();
            productType.setId(productTypeId);
            product.setProductType(productType);

            Producer producer = new Producer();
            producer.setId(producerId);
            product.setProducer(producer);

            ProductDAOImpl dao = new ProductDAOImpl();
            dao.updateProduct(product);

            request.getSession().setAttribute("editProductSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("editProductSuccess", false);
        }
        response.sendRedirect("quanlysanpham.jsp");
    }
}
