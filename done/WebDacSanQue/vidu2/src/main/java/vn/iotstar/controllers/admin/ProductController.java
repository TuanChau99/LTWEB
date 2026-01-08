package vn.iotstar.controllers.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List; 

import vn.iotstar.daos.CategoryDao;
import vn.iotstar.daos.ProductDAO;
import vn.iotstar.daos.impl.CategoryDaoImpl;
import vn.iotstar.daos.impl.ProductDaoImpl;
import vn.iotstar.entity.Product;
import vn.iotstar.models.Category;

@WebServlet(urlPatterns = {"/admin/product/add", "/admin/product/edit", "/admin/product/save"})
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ProductDAO productDao = new ProductDaoImpl();
    CategoryDao categoryDao = new CategoryDaoImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        
        // ⭐ Load danh sách danh mục để JSP hiển thị tên (Bún, Phở...)
        List<Category> categories = categoryDao.getAllCategories(); 
        req.setAttribute("categories", categories);

        if (url.contains("add")) {
            req.setAttribute("action", "add");
        } else if (url.contains("edit")) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam);
                    Product product = productDao.findById(id); // Lấy thông tin cũ để hiện lên form
                    req.setAttribute("product", product);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            req.setAttribute("action", "edit");
        }
        req.getRequestDispatcher("/views/admin/product-form.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        try {
            // Lấy dữ liệu từ các thẻ input name="..." trong JSP
            String idRaw = req.getParameter("id");
            String name = req.getParameter("name");
            String image = req.getParameter("image");
            double price = Double.parseDouble(req.getParameter("price"));
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            int cateID = Integer.parseInt(req.getParameter("cateID")); // Lấy ID từ dropdown
            int stock = Integer.parseInt(req.getParameter("stock"));

            Product p = new Product();
            p.setName(name);
            p.setImage(image);
            p.setPrice(price);
            p.setTitle(title);
            p.setDescription(description);
            p.setCateID(cateID); 
            p.setStock(stock);

            // Kiểm tra là thêm mới hay cập nhật
            if (idRaw == null || idRaw.isEmpty() || idRaw.equals("0")) {
                productDao.insert(p); 
            } else {
                p.setId(Integer.parseInt(idRaw));
                productDao.update(p); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Chuyển hướng về trang danh sách sau khi lưu thành công
        resp.sendRedirect(req.getContextPath() + "/admin/manageProduct");
    }
}