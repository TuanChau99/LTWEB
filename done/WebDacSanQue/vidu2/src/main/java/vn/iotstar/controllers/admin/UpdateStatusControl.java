package vn.iotstar.controllers.admin;

import vn.iotstar.daos.OrderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.daos.impl.OrderDaoImpl;

@WebServlet(name = "UpdateStatusControl", urlPatterns = {"/admin/updateStatus"})
public class UpdateStatusControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		try {
            // 1. Lấy dữ liệu từ tham số truyền vào
            String orderId = request.getParameter("oid");
            String status = request.getParameter("status");

            if (orderId != null && status != null) {
                // 2. Gọi DAO để cập nhật
                OrderDAO dao = new OrderDaoImpl();
                dao.updateStatus(Integer.parseInt(orderId), Integer.parseInt(status));

                // 3. Phản hồi cho AJAX (Sửa lỗi resp thành response)
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("success"); // Gửi chữ success về cho client
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("error");
        }
    }
}