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
        HttpSession session = req.getSession(false); // false = không tạo mới nếu chưa có

        UserModel user = (session != null) ? (UserModel) session.getAttribute("account") : null;

        if (user != null && user.getRoleid() == 1) {
            // Người dùng là admin
            chain.doFilter(request, response);
        } else {
            // Chưa đăng nhập hoặc không phải admin
            res.sendRedirect(req.getContextPath() + "/logout");
        }
    }
}
