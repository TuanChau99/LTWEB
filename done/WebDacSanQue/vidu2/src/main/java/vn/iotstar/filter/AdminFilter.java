package vn.iotstar.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.models.UserModel;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {

	    HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    HttpSession session = req.getSession(false); 

	    String url = req.getRequestURI();

	    // 1. Cho phép đi qua nếu là trang login hoặc tài nguyên tĩnh
	    // Lưu ý: Thêm "/login" vào đây để an toàn
	    if (url.contains("/login") || url.contains("/assets/") || url.contains("/image") || url.contains("/product")) {
	        chain.doFilter(request, response);
	        return;
	    }

	    UserModel user = (session != null) ? (UserModel) session.getAttribute("account") : null;

	    // 2. Kiểm tra quyền Admin (RoleID = 1)
	    if (user != null && user.getRoleid() == 1) {
	        chain.doFilter(request, response);
	    } else {
	        // Log để debug
	        System.out.println("❌ FILTER CHẶN: Path=" + url);
	        // Chuyển hướng về login
	        res.sendRedirect(req.getContextPath() + "/login");
	    }
	}
}