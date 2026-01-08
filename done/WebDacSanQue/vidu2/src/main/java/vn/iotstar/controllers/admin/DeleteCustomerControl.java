package vn.iotstar.controllers.admin;

import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.daos.UserDAO;
import vn.iotstar.daos.impl.UserDAOImpl;

@WebServlet(name = "DeleteCustomerControl", urlPatterns = {"/admin/customers/delete"})
public class DeleteCustomerControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Lấy ID khách hàng cần xóa từ URL
        String id = request.getParameter("id");
        
        if (id != null) {
            // 2. Gọi DAO thực hiện Xóa mềm (Soft Delete)
            UserDAO userDao = new UserDAOImpl();
            // Thay đổi từ delete sang softDelete để không mất dữ liệu đơn hàng
            userDao.softDelete(Integer.parseInt(id)); 
            System.out.println("Đã thực hiện soft delete cho ID: " + id); // Log để kiểm tra trong Console
        }
        
        // 3. Xóa xong thì quay về trang danh sách khách hàng
        response.sendRedirect(request.getContextPath() + "/admin/customers");
    }
}