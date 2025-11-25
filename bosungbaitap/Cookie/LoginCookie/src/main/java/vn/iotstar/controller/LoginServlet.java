package vn.iotstar.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy cookie đã lưu
        String savedUser = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("username")) {
                    savedUser = c.getValue();
                }
            }
        }
        request.setAttribute("savedUser", savedUser);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String remember = request.getParameter("remember");

        // Giả sử user=admin, pass=123
        if ("admin".equals(user) && "123".equals(pass)) {
            if ("on".equals(remember)) {
                Cookie cookie = new Cookie("username", user);
                cookie.setMaxAge(60 * 60 * 24 * 7); // lưu 7 ngày
                response.addCookie(cookie);
            }
            response.sendRedirect("welcome.jsp");
        } else {
            request.setAttribute("error", "Sai username hoặc password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
