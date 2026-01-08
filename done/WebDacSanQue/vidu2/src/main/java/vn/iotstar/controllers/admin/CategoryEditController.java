package vn.iotstar.controllers.admin;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.models.Category;
import vn.iotstar.services.CategoryService;
import vn.iotstar.services.impl.CategoryServiceImpl;
import vn.iotstar.utils.Constant;

@WebServlet("/admin/category/edit")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class CategoryEditController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryService cateService = new CategoryServiceImpl();
    private static final String SAVE_DIRECTORY = Constant.DIR + File.separator + "category";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Category cate = cateService.get(id);
        request.setAttribute("cate", cate);
        request.getRequestDispatcher("/views/admin/edit-category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");

        Category cate = cateService.get(id);
        cate.setName(name);

        // Xử lý upload file mới
        Part part = request.getPart("icon");
        String fileName = extractFileName(part);

        if (fileName != null && !fileName.isEmpty()) {
            File saveDir = new File(SAVE_DIRECTORY);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            part.write(SAVE_DIRECTORY + File.separator + fileName);
            cate.setIcon(fileName);
        }

        cateService.update(cate);
        response.sendRedirect(request.getContextPath() + "/admin/category/list");
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return null;
    }
}
