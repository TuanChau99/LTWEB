package vn.iotstar.controllers.admin;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.daos.OrderDAO; 
import vn.iotstar.daos.impl.OrderDaoImpl;

@WebServlet(urlPatterns = {"/admin/reports/sales"})
public class RevenueController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderDAO orderDao = new OrderDaoImpl();
        
        // 1. Lấy tổng doanh thu tháng này (Hàm bạn đã sửa lỗi SQL thành công)
        double monthlyRevenue = orderDao.getTotalRevenueThisMonth();
        
        // 2. Lấy dữ liệu 7 ngày gần nhất để vẽ biểu đồ
        Map<String, Double> dailyRevenue = orderDao.getRevenueLast7Days(); 
        
        // 3. Đẩy dữ liệu sang trang JSP
        req.setAttribute("monthlyRevenue", monthlyRevenue);
        req.setAttribute("dailyRevenueMap", dailyRevenue);
        
        // Chuyển tiếp sang file JSP báo cáo
        req.getRequestDispatcher("/views/admin/revenue-report.jsp").forward(req, resp);
    }
}