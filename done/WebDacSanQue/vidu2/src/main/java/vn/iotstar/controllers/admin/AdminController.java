package vn.iotstar.controllers.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.daos.ProductDAO;
import vn.iotstar.daos.OrderDAO; 
import vn.iotstar.daos.impl.OrderDaoImpl;
import vn.iotstar.daos.impl.ProductDaoImpl;
import vn.iotstar.daos.impl.UserDAOImpl;
import vn.iotstar.entity.Product;
import vn.iotstar.daos.UserDAO;
import vn.iotstar.models.OrderModel; 

@WebServlet(urlPatterns = {"/admin"})
public class AdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Khởi tạo các đối tượng DAO
        ProductDAO dao = new ProductDaoImpl();
        OrderDAO orderDao = new OrderDaoImpl();
        UserDAO userDao = new UserDAOImpl(); // Khởi tạo để lấy khách hàng mới
        

        // --- PHẦN 1: XỬ LÝ SỐ LƯỢNG ĐƠN HÀNG HIỂN THỊ ---
        // Lấy số lượng từ dropdown (mặc định là 5 nếu người dùng chưa chọn)
        String entries = req.getParameter("entries");
        String keyword = req.getParameter("search");
        int limit = (entries != null) ? Integer.parseInt(entries) : 5;

        // Gọi DAO lấy danh sách đơn hàng mới nhất dựa theo limit
        List<OrderModel> recentOrders = orderDao.getRecentOrders(limit, keyword);

        // --- PHẦN 2: LẤY DỮ LIỆU KPI & KHO HÀNG ---
        List<Product> lowStock = dao.getLowStock();
        List<Product> allProducts = dao.getAll();

        // Lấy tổng doanh thu tháng này (đã sửa lỗi SQL dùng total_price)
        double totalRevenue = orderDao.getTotalRevenueThisMonth();
        
        // 2. Tổng số loại sản phẩm
        int totalProducts = allProducts.size();
        
       // 3. Đếm số đơn hàng chờ xử lý (Mới cập nhật)
        int pendingOrders = orderDao.countPendingOrders();
        
        int newCustomers = userDao.countNewCustomers();

        // --- PHẦN 3: ĐẨY DỮ LIỆU SANG JSP ---
        // Dữ liệu kho hàng & KPIs
        req.setAttribute("lowStockList", lowStock);
        req.setAttribute("allProduct", allProducts);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalProductsCount", totalProducts);
        req.setAttribute("pendingOrders", pendingOrders);
        req.setAttribute("newCustomersCount", newCustomers);
        
        // Dữ liệu Đơn hàng mới nhất
        req.setAttribute("recentOrders", recentOrders);
        req.setAttribute("selectedLimit", limit); // Gửi lại để giữ trạng thái dropdown
        req.setAttribute("keyword", keyword); // Gửi lại keyword để hiển thị trên ô input

        // Chuyển tiếp sang trang views/admin.jsp
        req.getRequestDispatcher("/views/admin.jsp").forward(req, resp);
    }
}