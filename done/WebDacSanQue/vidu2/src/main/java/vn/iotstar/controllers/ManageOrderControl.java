package vn.iotstar.controllers;

import vn.iotstar.daos.OrderDAO;
import vn.iotstar.entity.Order;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.daos.impl.OrderDaoImpl;

@WebServlet(name = "ManageOrderControl", urlPatterns = {"/admin/manageOrder"})
public class ManageOrderControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 1. Gọi DAO để lấy danh sách đơn hàng
        OrderDAO dao = new OrderDaoImpl();
        List<Order> list = dao.getAllOrders();
        
        // 2. Đẩy danh sách vào request để JSP có thể đọc được
        request.setAttribute("listO", list);
        
        // 3. Chuyển hướng đến trang manage-order.jsp
        request.getRequestDispatcher("/views/admin/manage-order.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}