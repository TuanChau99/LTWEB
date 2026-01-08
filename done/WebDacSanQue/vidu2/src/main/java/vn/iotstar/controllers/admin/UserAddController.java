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

@WebServlet("/admin/user/add")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UserAddController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/admin/user-add.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    req.setCharacterEncoding("UTF-8");
	    resp.setCharacterEncoding("UTF-8");

	    try {
	        UserModel user = new UserModel();
	        user.setUsername(req.getParameter("username"));
	        user.setFullname(req.getParameter("fullname"));
	        user.setEmail(req.getParameter("email"));
	        user.setPassword(req.getParameter("password"));
	        user.setPhone(req.getParameter("phone"));
	        
	        // ⭐ QUAN TRỌNG: Gán ngày tạo mặc định ngay tại đây để tránh lỗi SQL null createdDate
	        user.setCreateddate(new java.sql.Date(System.currentTimeMillis()));

	        String roleidRaw = req.getParameter("roleid");
	        user.setRoleid(roleidRaw != null ? Integer.parseInt(roleidRaw) : 5);

	        // Xử lý Upload Avatar
	        Part part = req.getPart("avatar");
	        if (part != null && part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
	            String fileName = System.currentTimeMillis() + "_" + part.getSubmittedFileName();
	            String uploadPath = "D:/upload";
	            File uploadDir = new File(uploadPath);
	            if (!uploadDir.exists()) uploadDir.mkdirs(); 

	            part.write(uploadPath + File.separator + fileName);
	            user.setAvatar(fileName);
	        } else {
	            user.setAvatar("null"); // Nếu không chọn file, để là null
	        }

	        System.out.println(">>> DANG CHUAN BI LUU USER: " + user.getUsername());

	        // Lệnh này sẽ gọi qua Service -> Dao
	        userService.insert(user);

	        System.out.println(">>> DA GOI LENH INSERT XONG!");
	        resp.sendRedirect(req.getContextPath() + "/admin/user/list");

	    } catch (Exception e) {
	        System.err.println("❌ Lỗi tại Controller: " + e.getMessage());
	        e.printStackTrace();
	        req.setAttribute("error", "Lỗi: " + e.getMessage());
	        req.getRequestDispatcher("/views/admin/user-add.jsp").forward(req, resp);
	    }
	}
}