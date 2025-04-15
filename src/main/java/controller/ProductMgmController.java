package controller;

import dao.impl.ProductDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductMgmController", urlPatterns = {"/admin/products"})
public class ProductMgmController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = new ProductDAOImpl().findAll();
        request.setAttribute("products", products);
        request.getRequestDispatcher("quanlysanpham.jsp").forward(request, response);
    }
}
