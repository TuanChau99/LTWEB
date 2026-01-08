package vn.iotstar.controllers;

import vn.iotstar.daos.OrderDAO;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderDetail;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.daos.impl.OrderDaoImpl;

@WebServlet(name = "OrderDetailControl", urlPatterns = {"/admin/orderDetail"})
public class OrderDetailControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 1. Lấy oid từ tham số trên URL (ví dụ: orderDetail?oid=1)
        String orderId = request.getParameter("oid");
        int oid = Integer.parseInt(orderId);
        
        // 2. Gọi DAO để lấy danh sách chi tiết món hàng
        OrderDAO dao = new OrderDaoImpl();
     // BỔ SUNG: Lấy thông tin chung của đơn hàng để hiển thị Header/Địa chỉ
        Order orderInfo = dao.getOrderById(oid);
        
        List<OrderDetail> list = dao.getOrderDetailByOrderId(Integer.parseInt(orderId));
        
        // 3. Đẩy dữ liệu vào request
        request.setAttribute("listD", list);
        request.setAttribute("orderInfo", orderInfo); // Biến này dùng cho Header
        
        // 4. Chuyển hướng sang trang hiển thị chi tiết
        request.getRequestDispatcher("/views/admin/order-detail.jsp").forward(request, response);
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