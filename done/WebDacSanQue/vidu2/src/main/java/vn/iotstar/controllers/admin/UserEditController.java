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
import vn.iotstar.models.UserModel;
import vn.iotstar.services.UserService;
import vn.iotstar.services.impl.UserServiceImpl;
import vn.iotstar.utils.Constant;

@WebServlet("/admin/user/edit")
@MultipartConfig
public class UserEditController extends HttpServlet {
    UserService userService = new UserServiceImpl();
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        
        // Kiểm tra tham số ID đầu vào
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/user/list");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            UserModel user = userService.findById(id);

            if (user != null) {
                // Chỉ in Log khi user khác null
                System.out.println("DEBUG doGet: Tìm thấy User ID = " + user.getId());
                req.setAttribute("user", user);
                req.getRequestDispatcher("/views/admin/user-edit.jsp").forward(req, resp);
            } else {
                // Nếu không tìm thấy user trong DB 
                System.out.println("DEBUG doGet: Không tìm thấy User với ID = " + id);
                resp.sendRedirect(req.getContextPath() + "/admin/user/list?msg=notfound");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/user/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        UserModel user = new UserModel();
        
        // Lấy ID từ form (Quan trọng để không bị ID=0)
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            user.setId(Integer.parseInt(idStr));
        }
        
        user.setUsername(req.getParameter("username")); 
        user.setFullname(req.getParameter("fullname"));
        user.setEmail(req.getParameter("email"));
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        
        String roleIdStr = req.getParameter("roleid");
        if (roleIdStr != null && !roleIdStr.isEmpty()) {
            user.setRoleid(Integer.parseInt(roleIdStr));
        }
        
        // Xử lý ảnh đại diện
        String oldAvatar = req.getParameter("oldAvatar");
        Part part = req.getPart("avatar");
        
        if (part != null && part.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + part.getSubmittedFileName();
            part.write(Constant.DIR + File.separator + fileName);
            user.setAvatar(fileName);
        } else {
            user.setAvatar(oldAvatar);
        }
        
        // Kiểm tra lần cuối trước khi gọi update
        System.out.println("DEBUG doPost: Cập nhật User ID = " + user.getId());
        
        if(user.getId() > 0) {
            userService.update(user);
        }
        
        resp.sendRedirect(req.getContextPath() + "/admin/user/list");
    }
}