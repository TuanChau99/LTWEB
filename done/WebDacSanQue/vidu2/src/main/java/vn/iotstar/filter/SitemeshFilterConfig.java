package vn.iotstar.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
//Ví dụ: Tạo SitemeshFilterConfig (chỉ áp dụng nếu bạn không muốn sửa Sitemesh filter gốc)
@WebFilter(urlPatterns = {"/productss/*", "/productssssss"}) 
public class SitemeshFilterConfig implements Filter {

 @Override
 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
         throws IOException, ServletException {
     
     HttpServletRequest req = (HttpServletRequest) request;
     
     // 1. Kiểm tra tham số Load More
     String isLoadMore = req.getParameter("loadMore");
     
     if ("true".equals(isLoadMore)) {
         // 2. Vô hiệu hóa Sitemesh: 
         // Đặt thuộc tính này để nói với Sitemesh decorator (hoặc Filter) 
         // rằng không cần trang trí trang này.
         req.setAttribute("sitemesh.decorator", "none"); 
     }

     chain.doFilter(request, response);
 }
 // ... (init và destroy)
}