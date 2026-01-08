package vn.iotstar.controllers.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.daos.ProductDAO;
import vn.iotstar.daos.impl.ProductDaoImpl;

@WebServlet(name = "DeleteProductControl", urlPatterns = {"/admin/deleteProduct"})
public class DeleteProductControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 1. Lấy mã sản phẩm cần xóa từ tham số 'pid' trên URL
        String pid = request.getParameter("pid");
        
        // 2. Gọi DAO để thực hiện lệnh xóa trong Database
        ProductDAO dao = new ProductDaoImpl();
        try {
            int id = Integer.parseInt(pid);
            dao.delete(id);
            
            // In log để kiểm tra trên Console
            System.out.println("✅ Đã xóa thành công sản phẩm ID: " + id);
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        // 3. Chuyển hướng quay lại trang quản lý sản phẩm để cập nhật danh sách
        response.sendRedirect("manageProduct");
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
