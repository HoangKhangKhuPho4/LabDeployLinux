package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "EditOrderControll", value = "/oderedit")
public class EditOrderControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
//        OrderDAO orderDAO = new OrderDAO();

        String id = request.getParameter("id");
        String userId = request.getParameter("userId");
        String address = request.getParameter("address");
        String orderStatus = request.getParameter("status");
        String payment = request.getParameter("payment");
        Date orderDate = Date.valueOf(request.getParameter("dateOder"));
        Date deliveryDate = Date.valueOf(request.getParameter("doneDate"));

//        User user = UserDAO.getById(userId);
//        if(user != null){
//            Order order = new Order(id, user, address, orderStatus, payment, orderDate, deliveryDate);
//            orderDAO.update(order);
        response.sendRedirect("success.jsp");
//        }else{
//            response.sendRedirect("error.jsp");
    }
//    }
}