package vn.iotstar.controllers.admin;

import vn.iotstar.daos.UserDAO;
import vn.iotstar.daos.impl.UserDAOImpl;
import vn.iotstar.models.UserModel;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CustomerControl", urlPatterns = {"/admin/customers"})
public class CustomerControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 1. Khởi tạo đối tượng Dao từ các file bạn đã có
        UserDAO userDao = new UserDAOImpl();
        
        // 2. Gọi hàm lấy danh sách khách hàng (những người có roleid = 5)
        List<UserModel> list = userDao.getAllCustomers();
        
        // 3. Đưa danh sách vào request attribute để JSP lấy dữ liệu
        request.setAttribute("listC", list);
        
        // 4. Chuyển hướng đến trang JSP mà chúng ta đã làm ở bước trước
        request.getRequestDispatcher("/views/admin/customers.jsp").forward(request, response);
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