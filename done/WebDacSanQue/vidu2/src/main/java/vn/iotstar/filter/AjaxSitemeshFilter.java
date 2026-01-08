package vn.iotstar.filter; // Hoặc package vn.iotstar.controllers

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

// Ánh xạ đến các URL mà bạn muốn áp dụng logic Load More
// Ở đây là Controller /product (danh sách sản phẩm)
@WebFilter(urlPatterns = {"/product/*", "/product"}) 
public class AjaxSitemeshFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AjaxSitemeshFilter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        
        // 1. Kiểm tra tham số Load More
        String isLoadMore = req.getParameter("loadMore");
        
        if ("true".equals(isLoadMore)) {
            // 2. Vô hiệu hóa Sitemesh: 
            // Đặt thuộc tính này sẽ được Sitemesh Filter nhận biết.
            req.setAttribute("sitemesh.decorator", "none"); 
            System.out.println("Sitemesh disabled for AJAX request: " + req.getRequestURI());
        }

        // Chuyển quyền xử lý sang Filter tiếp theo (Bao gồm Sitemesh Filter)
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("AjaxSitemeshFilter destroyed.");
    }
}