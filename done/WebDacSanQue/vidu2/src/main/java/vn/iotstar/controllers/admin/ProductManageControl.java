package vn.iotstar.controllers.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.daos.ProductDAO;
import vn.iotstar.daos.impl.ProductDaoImpl;
import vn.iotstar.entity.Product;

@WebServlet(name = "ProductManageControl", urlPatterns = {"/admin/manageProduct"})
public class ProductManageControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 1. Khởi tạo DAO
        ProductDAO dao = new ProductDaoImpl();
        
        // 2. Lấy danh sách tất cả sản phẩm
        List<Product> list = dao.getAll();
        
        // 3. Đẩy danh sách sản phẩm vào request để JSP sử dụng
        request.setAttribute("listP", list);
        
        // 4. Chuyển hướng đến trang JSP quản lý sản phẩm
        // Đảm bảo đường dẫn này khớp với vị trí file jsp của bạn
        request.getRequestDispatcher("/views/admin/manage-product.jsp").forward(request, response);
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
