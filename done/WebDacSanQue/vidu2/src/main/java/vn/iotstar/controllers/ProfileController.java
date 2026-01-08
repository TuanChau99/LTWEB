package vn.iotstar.controllers;

import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.daos.OrderDAO;
import vn.iotstar.daos.UserDAO;
import vn.iotstar.daos.impl.OrderDaoImpl;
import vn.iotstar.daos.impl.UserDAOImpl;
import vn.iotstar.models.OrderModel;
import vn.iotstar.models.UserModel;

@WebServlet(urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserModel account = (UserModel) session.getAttribute("account");
        
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        OrderDAO orderDao = new OrderDaoImpl(); 
        List<OrderModel> listOrders = orderDao.findByUserId(account.getId());
        
        req.setAttribute("listOrders", listOrders);
        // Đảm bảo đường dẫn này khớp với thư mục của bạn
        req.getRequestDispatcher("/views/web/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        UserModel account = (UserModel) session.getAttribute("account");

        // CHỐNG LỖI NULLPOINTEREXCEPTION
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address"); 

        UserDAO dao = new UserDAOImpl();
        dao.updateProfile(account.getId(), fullname, email, phone, address);
        
        // CẬP NHẬT LẠI SESSION ĐỂ HIỂN THỊ NGAY
        account.setFullname(fullname);
        account.setEmail(email);
        account.setPhone(phone);
        account.setAddress(address); // Cần có hàm này trong UserModel
        session.setAttribute("account", account);
        
        req.setAttribute("message", "Cập nhật hồ sơ thành công!");
        // Gọi lại doGet để load lại dữ liệu đơn hàng và hiển thị message
        doGet(req, resp);
    }
}