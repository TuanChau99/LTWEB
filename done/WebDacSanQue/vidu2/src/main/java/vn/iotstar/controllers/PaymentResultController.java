package vn.iotstar.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/payment-result")
public class PaymentResultController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        
        // Lấy dữ liệu từ Session ra và đưa vào Request để trang JSP hiển thị
        String status = (String) session.getAttribute("paymentStatus");
        String message = (String) session.getAttribute("paymentMessage");
        Object orderId = session.getAttribute("paymentOrderId");

        // Nếu không có dữ liệu (truy cập trực tiếp trái phép), cho về trang chủ
        if (status == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        req.setAttribute("status", status);
        req.setAttribute("message", message);
        req.setAttribute("orderId", orderId);

        // Xóa ngay dữ liệu trong Session để F5 không bị lặp lại
        session.removeAttribute("paymentStatus");
        session.removeAttribute("paymentMessage");
        session.removeAttribute("paymentOrderId");

        req.getRequestDispatcher("/views/web/payment-result.jsp").forward(req, resp);
    }
}