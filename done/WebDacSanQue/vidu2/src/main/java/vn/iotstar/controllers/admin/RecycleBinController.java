package vn.iotstar.controllers.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.daos.ProductDAO;
import vn.iotstar.daos.UserDAO;
import vn.iotstar.daos.impl.ProductDaoImpl;
import vn.iotstar.daos.impl.UserDAOImpl;

@WebServlet(urlPatterns = {"/admin/recycle-bin", "/admin/product/restore", "/admin/user/restore"})
public class RecycleBinController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	ProductDAO productDao = new ProductDaoImpl();
    UserDAO userDao = new UserDAOImpl(); 

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("/admin/product/restore")) {
            int id = Integer.parseInt(req.getParameter("id"));
            productDao.restore(id); // Gọi hàm restore sản phẩm
            resp.sendRedirect(req.getContextPath() + "/admin/recycle-bin");
        } 
        else if (url.contains("/admin/user/restore")) {
            int id = Integer.parseInt(req.getParameter("id"));
            
            // UPDATE [User] SET isDeleted = 'False' WHERE id = ?
            userDao.restoreUser(id); 
            resp.sendRedirect(req.getContextPath() + "/admin/recycle-bin");
        } 
        else {
            // Lấy cả 2 danh sách để hiển thị chung một trang
            req.setAttribute("deletedProducts", productDao.getDeletedProducts());
            req.setAttribute("deletedUsers", userDao.getDeletedUsers()); // Cần viết hàm này trong UserDAOImpl
            req.getRequestDispatcher("/views/admin/recycle-bin-all.jsp").forward(req, resp);
        }
    }
}
