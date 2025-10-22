package vn.iotstar.filter;

import java.io.IOException;
import java.util.Date;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter("/*") // Áp dụng cho toàn bộ request
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("✅ LogFilter init!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String servletPath = req.getServletPath();

        System.out.println("📅 " + new Date() + " | Path: " + servletPath + " | URL: " + req.getRequestURL());

        // Cho phép request đi tiếp
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("❌ LogFilter destroy!");
    }
}
