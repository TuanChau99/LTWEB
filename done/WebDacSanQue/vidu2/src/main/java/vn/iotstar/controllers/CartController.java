package vn.iotstar.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.iotstar.daos.DAO; 
import vn.iotstar.entity.Product; 
import vn.iotstar.entity.Cart; 

@WebServlet("/cart") 
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
           throws ServletException, IOException {
        
        // Lấy Session
        HttpSession session = request.getSession();
        // Lấy Giỏ hàng từ Session, nếu chưa có thì tạo mới
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        
        String action = request.getParameter("action");
        String productIdStr = request.getParameter("id");
        
        if (action != null && productIdStr != null) {
            try {
                int id = Integer.parseInt(productIdStr);
                DAO dao = new DAO();
                
                switch (action) {
                    case "add":
                        // 1. Lấy thông tin Sản phẩm từ Database
                        Product product = dao.getProductByID(id); 
                        if (product != null) {
                             // 2. Thêm vào giỏ hàng
                            cart.addProduct(product);
                            
                            // ⭐ LOGIC MỚI: Xử lý chuyển hướng linh hoạt
                            String referer = request.getHeader("referer");
                            String defaultRedirect = request.getContextPath() + "/product?success=add"; // Mặc định chuyển về trang /product
                            
                            // Kiểm tra Referer để chuyển hướng về trang trước đó
                            if (referer != null && !referer.contains("/cart")) {
                                // 3. Thêm tham số báo thành công vào URL Referer
                                String redirectUrl = referer;
                                if (redirectUrl.contains("?")) {
                                    redirectUrl += "&success=add";
                                } else {
                                    redirectUrl += "?success=add";
                                }
                                response.sendRedirect(redirectUrl);
                            } else {
                                // Chuyển hướng về trang danh sách sản phẩm nếu không xác định được Referer
                                response.sendRedirect(defaultRedirect);
                            }
                        }
                        return; // Kết thúc xử lý
                        
                    case "delete":
                        cart.removeItem(id);
                        break;
                        
                    case "update":
                        String quantityStr = request.getParameter("quantity");
                        int quantity = Integer.parseInt(quantityStr);
                        cart.updateQuantity(id, quantity);
                        break;
                }
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu id hoặc quantity không phải số
            }
        }
        
        // Nếu không phải là action "add", forward đến trang giỏ hàng để xem
        request.getRequestDispatcher("/views/web/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
           throws ServletException, IOException {
        doGet(request, response);
    }
}