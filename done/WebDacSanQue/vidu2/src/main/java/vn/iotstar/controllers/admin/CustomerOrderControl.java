package vn.iotstar.controllers.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.daos.OrderDAO;
import vn.iotstar.entity.Order;
import java.io.IOException;
import java.util.List;
import vn.iotstar.daos.impl.OrderDaoImpl;

@WebServlet(name = "CustomerOrderControl", urlPatterns = {"/admin/customer/orders"})
public class CustomerOrderControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Lấy ID khách hàng từ tham số 'id' trên URL
        String userIdStr = request.getParameter("id");
        
        if (userIdStr != null) {
            int userId = Integer.parseInt(userIdStr);
            
            // 2. Gọi OrderDAO để lấy danh sách đơn hàng của User này
            OrderDAO orderDAO = new OrderDaoImpl();
            List<Order> listO = orderDAO.getOrdersByUserId(userId);
            
            // 3. Đẩy dữ liệu sang trang JSP để hiển thị
            request.setAttribute("listOrders", listO);
            request.setAttribute("uid", userId); // Gửi thêm ID để làm nút 'Quay lại'
            request.getRequestDispatcher("/views/admin/customer-orders.jsp").forward(request, response);
        }
    }
}