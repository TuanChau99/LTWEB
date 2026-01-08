package vn.iotstar.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Thêm import session

import vn.iotstar.daos.OrderDAO;
import vn.iotstar.daos.impl.OrderDaoImpl;

@WebServlet("/vnpay-return")
public class VnPayReturnController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        
        String vnp_ResponseCode = req.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = req.getParameter("vnp_TxnRef"); 
        
        String status = "fail"; // Mặc định là thất bại
        String message = "";
        int orderId = 0;

        try {
            if (vnp_TxnRef != null) {
                String orderIdStr = vnp_TxnRef.split("_")[0];
                orderId = Integer.parseInt(orderIdStr);
                OrderDAO orderDAO = new OrderDaoImpl();

                if ("00".equals(vnp_ResponseCode)) {
                    // THÀNH CÔNG: Cập nhật status sang "Đang giao" (1)
                    orderDAO.updateStatus(orderId, 1); 
                    session.removeAttribute("cart"); // Xóa giỏ hàng khỏi session
                    status = "success";
                    message = "Thanh toán thành công đơn hàng #" + orderId;
                } 
                else {
                    // THẤT BẠI HOẶC HỦY (Mã 24): Xóa đơn hàng tạm để tránh rác DB
                    orderDAO.deleteOrder(orderId); 
                    status = "fail";
                    if ("24".equals(vnp_ResponseCode)) {
                        message = "Bạn đã hủy giao dịch. Đơn hàng #" + orderId + " đã được loại bỏ.";
                    } else {
                        message = "Giao dịch thất bại (Mã lỗi: " + vnp_ResponseCode + ")";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = "error";
            message = "Đã xảy ra lỗi hệ thống khi xử lý thanh toán.";
        }
        
        // --- PHẦN BẠN YÊU CẦU: LƯU VÀO SESSION VÀ REDIRECT ---
        session.setAttribute("paymentStatus", status); 
        session.setAttribute("paymentMessage", message);
        session.setAttribute("paymentOrderId", orderId);

        // Redirect để xóa sạch các tham số vnp_... trên thanh địa chỉ
        resp.sendRedirect(req.getContextPath() + "/payment-result");
    }
}