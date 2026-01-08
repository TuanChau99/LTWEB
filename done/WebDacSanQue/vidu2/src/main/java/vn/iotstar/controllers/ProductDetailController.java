package vn.iotstar.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.daos.DAO;
import vn.iotstar.entity.Product;

@WebServlet("/product-detail")
public class ProductDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("home"); // Chuyển hướng nếu không có ID
            return;
        }

        try {
            int productId = Integer.parseInt(idStr);
            DAO dao = new DAO();
            
            // --- 1. Lấy thông tin chi tiết sản phẩm ---
            Product product = dao.getProductByID(productId);

            if (product == null) {
                response.sendRedirect("home"); // Chuyển hướng nếu không tìm thấy
                return;
            }

            // --- 2. Lấy Hàng cùng loại (Cùng Category) ---
            // Dùng getCate_id() để lấy ID danh mục
            List<Product> relatedProducts = dao.getRelatedProductsByCID(product.getCateID(), productId);
            
            // --- 3. Lấy Hàng cùng nhà cung cấp (Seller/Supplier) ---
            // ⭐ ĐÃ SỬA LỖI: Thay thế cú pháp sai bằng phương thức đúng: product.getSell_id()
            List<Product> supplierProducts = dao.getProductsBySupplier(product.getSell_id(), productId);

            // --- 4. Xử lý Hàng đã xem (Cookies) ---
            List<Product> viewedProducts = processViewedProducts(request, response, product);
            
            // --- 5. Đặt Attributes và Forward ---
            request.setAttribute("product", product);
            request.setAttribute("relatedProducts", relatedProducts);
            request.setAttribute("supplierProducts", supplierProducts);
            request.setAttribute("viewedProducts", viewedProducts); 
            
            request.getRequestDispatcher("/views/web/product-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    // Hàm xử lý Cookie cho Hàng đã xem (Giữ nguyên)
    private List<Product> processViewedProducts(HttpServletRequest request, HttpServletResponse response, Product currentProduct) {
        DAO dao = new DAO();
        StringBuilder cookieValue = new StringBuilder();
        List<Product> viewedProducts = new ArrayList<>();
        
        // 1. Đọc Cookie hiện tại
        Cookie[] cookies = request.getCookies();
        Cookie viewedCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("viewedProducts".equals(cookie.getName())) {
                    viewedCookie = cookie;
                    cookieValue.append(cookie.getValue());
                    break;
                }
            }
        }
        
        // 2. Cập nhật Cookie
        String currentIds = cookieValue.toString();
        String currentPidStr = String.valueOf(currentProduct.getId());
        
        // Xóa sản phẩm hiện tại khỏi danh sách nếu đã tồn tại để tránh trùng lặp và đưa lên đầu
        String newIds = currentIds.replace("-" + currentPidStr, "").replace(currentPidStr, "");
        
        // Thêm ID sản phẩm hiện tại vào đầu danh sách
        if (!newIds.isEmpty()) {
            newIds = currentPidStr + "-" + newIds;
        } else {
            newIds = currentPidStr;
        }

        // Giới hạn chỉ giữ 5 ID gần nhất (tùy chọn)
        String[] idArray = newIds.split("-");
        int limit = Math.min(idArray.length, 5);
        StringBuilder finalIds = new StringBuilder();
        
        for (int i = 0; i < limit; i++) {
            if (i > 0) finalIds.append("-");
            finalIds.append(idArray[i]);
            
            // Lấy dữ liệu sản phẩm để hiển thị (trừ sản phẩm đang xem)
            if (!idArray[i].equals(currentPidStr)) {
                try {
                    Product viewedP = dao.getProductByID(Integer.parseInt(idArray[i]));
                    if(viewedP != null) {
                        viewedProducts.add(viewedP);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        
        // 3. Ghi lại Cookie
        if (viewedCookie == null) {
            viewedCookie = new Cookie("viewedProducts", finalIds.toString());
        } else {
            viewedCookie.setValue(finalIds.toString());
        }
        
        // Thiết lập thời gian sống của cookie (ví dụ: 1 tuần = 60*60*24*7)
        viewedCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(viewedCookie);
        
        return viewedProducts;
    }
}