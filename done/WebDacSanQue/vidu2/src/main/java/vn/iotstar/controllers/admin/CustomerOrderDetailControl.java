package vn.iotstar.controllers.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.daos.OrderDAO;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderDetail;
import java.io.IOException;
import java.util.List;
import vn.iotstar.daos.impl.OrderDaoImpl;

@WebServlet(name = "CustomerOrderDetailControl", urlPatterns = {"/admin/customer/orderDetail"})
public class CustomerOrderDetailControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String orderIdStr = request.getParameter("id");
        
        if (orderIdStr != null) {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO orderDAO = new OrderDaoImpl();

            // 1. Lấy danh sách sản phẩm chi tiết
            List<OrderDetail> listDetails = orderDAO.getOrderDetailByOrderId(orderId);
            
            // 2. Lấy thông tin đơn hàng tổng quát (để lấy tên khách hàng, ngày đặt)
            // Tìm trong danh sách tất cả đơn hàng cái có ID trùng khớp
            Order orderInfo = orderDAO.getAllOrders().stream()
                                .filter(o -> o.getId() == orderId)
                                .findFirst()
                                .orElse(null);
            
            // 3. Đẩy dữ liệu sang JSP
            request.setAttribute("listD", listDetails);
            request.setAttribute("orderInfo", orderInfo); // Chứa thông tin khách hàng
            request.setAttribute("orderId", orderId);
            
            request.getRequestDispatcher("/views/admin/customer-order-detail.jsp").forward(request, response);
        }
    }
}