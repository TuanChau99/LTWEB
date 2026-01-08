package vn.iotstar.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.iotstar.daos.OrderDAO;
import vn.iotstar.daos.ProductDAO;
import vn.iotstar.entity.Cart;
import vn.iotstar.entity.Product;
import vn.iotstar.models.UserModel; 
import vn.iotstar.daos.impl.OrderDaoImpl;
import vn.iotstar.daos.impl.ProductDaoImpl;

@WebServlet(name = "CheckoutControl", urlPatterns = {"/checkout"})
public class CheckoutControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // 1. Nhận phương thức thanh toán và ĐỊA CHỈ nhận hàng (Mới thêm)
        String paymentMethod = request.getParameter("paymentMethod");
        String address = request.getParameter("address"); // Nhận chuỗi "Tên | SĐT | Địa chỉ"
        
        // 2. Kiểm tra giỏ hàng
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }
        
        // 3. Kiểm tra đăng nhập
        UserModel user = (UserModel) session.getAttribute("account"); 
        if (user == null) {
            session.setAttribute("redirectAfterLogin", "cart"); 
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 4. Kiểm tra tồn kho
        ProductDAO pDao = new ProductDaoImpl();
        String errorMsg = null;
        for (Product p : cart.getItems().values()) {
            int stockTrongKho = pDao.getProductStock(p.getId()); 
            if (p.getQuantity() > stockTrongKho) {
                errorMsg = "Sản phẩm " + p.getName() + " chỉ còn " + stockTrongKho + " món. Vui lòng cập nhật lại giỏ hàng!";
                break; 
            }
        }

        if (errorMsg != null) {
            session.setAttribute("errorStock", errorMsg); 
            response.sendRedirect(request.getContextPath() + "/cart"); 
            return;
        }

        // 5. TẠO ĐƠN HÀNG TRONG DB
        OrderDAO oDao = new OrderDaoImpl();
        
        // Đã sửa: Truyền thêm tham số address để khớp với DAO
        // Cột address trong SQL sẽ không còn bị NULL nữa
        int orderId = oDao.insertOrder(user, cart, address); 

        if (orderId > 0) {
            double totalMoney = cart.getTotalMoney();

            // 6. PHÂN LUỒNG XỬ LÝ
            if ("VNPAY".equalsIgnoreCase(paymentMethod)) {
                // Chuyển hướng sang trang thanh toán VNPAY
                response.sendRedirect(request.getContextPath() + "/vnpay-payment?amount=" + totalMoney + "&orderId=" + orderId);
            } 
            else {
                // Xử lý COD (Thanh toán khi nhận hàng)
                session.removeAttribute("cart");
                
                // Sử dụng Redirect sang trang kết quả giống như VNPAY để URL gọn gàng
                session.setAttribute("paymentStatus", "success");
                session.setAttribute("paymentMessage", "Đặt hàng thành công! Đơn hàng #" + orderId + " đang được xử lý.");
                session.setAttribute("paymentOrderId", orderId);
                
                response.sendRedirect(request.getContextPath() + "/payment-result");
            }
        } else {
            session.setAttribute("errorStock", "Không thể tạo đơn hàng. Vui lòng thử lại sau!");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}