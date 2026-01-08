package vn.iotstar.controllers.admin;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.models.Category;
import vn.iotstar.services.CategoryService;
import vn.iotstar.services.impl.CategoryServiceImpl;
import vn.iotstar.utils.Constant;

@WebServlet("/admin/category/delete")
public class CategoryDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryService cateService = new CategoryServiceImpl();
    private static final String SAVE_DIRECTORY = Constant.DIR + File.separator + "category";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Category cate = cateService.get(id);

        if (cate != null) {
            // Xóa file icon nếu tồn tại
            if (cate.getIcon() != null && !cate.getIcon().isEmpty()) {
                File file = new File(SAVE_DIRECTORY + File.separator + cate.getIcon());
                if (file.exists()) {
                    file.delete();
                }
            }
            // Xóa record trong DB
            cateService.delete(id);
        }

        response.sendRedirect(request.getContextPath() + "/admin/category/list");
    }
}
