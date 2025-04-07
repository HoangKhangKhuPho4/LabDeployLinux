package controller;

import dao.IProductTypeDAO;
import dao.impl.ProductTypeDAOImpl;
import model.Product;
import model.ProductType;
import service.IProductService;
import service.impl.ProductServiceImpl;
import utils.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Type", value = "/type")
public class Type extends HttpServlet {
    private IProductService productService = new ProductServiceImpl();
    private IProductTypeDAO productTypeDAO = new ProductTypeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productTypeId = Integer.parseInt(request.getParameter("id"));
        ProductType productType = productTypeDAO.findById(productTypeId);
        List<Product> listProducts = productService.findByCategory(productTypeId);
        User user = (User) SessionUtil.getInstance().getKey(request, "user");
        request.getRequestDispatcher("danhmuctheoloaisanpham.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
