package controller;

import dao.impl.ProductDAOImpl;
import model.Product;
import service.IProductService;
import service.impl.ProductServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Category", value = "/producer")
public class Category extends HttpServlet {
    private IProductService productService = new ProductServiceImpl();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer producerId = Integer.parseInt(request.getParameter("id"));
        List<Product> listProduct = productService.findByProducer(producerId);
        request.setAttribute("listProductByProducer", listProduct);
        request.getRequestDispatcher("danhmuctheoNSX.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
