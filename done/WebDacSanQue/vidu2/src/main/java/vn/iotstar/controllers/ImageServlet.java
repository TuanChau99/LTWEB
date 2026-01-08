package vn.iotstar.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.utils.Constant;

@WebServlet(urlPatterns = "/image")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Lấy tên file từ tham số ?fname=...
        String fileName = request.getParameter("fname");

        // 2. Kiểm tra nếu file name rỗng hoặc là chuỗi "null" do DB lưu
        if (fileName == null || fileName.isEmpty() || fileName.equals("null")) {
            return; // Không làm gì cả, trình duyệt sẽ tự kích hoạt onerror trên thẻ <img>
        }

        // 3. Đường dẫn gốc (Ví dụ: D:/upload)
        // Bỏ phần "category" để Servlet tìm được cả ảnh User và Category lưu tại D:/upload
        File file = new File(Constant.DIR, fileName);

        // 4. Kiểm tra file có tồn tại vật lý trên ổ đĩa không
        if (!file.exists()) {
            // Log ra console để bạn dễ dàng kiểm tra đường dẫn bị sai ở đâu
            System.err.println("===> ImageServlet: File không tồn tại tại: " + file.getAbsolutePath());
            return;
        }

        // 5. Tự động xác định kiểu nội dung (MIME type) như image/jpeg, image/png
        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);
        response.setContentLengthLong(file.length());

        // 6. Đọc file và ghi luồng dữ liệu ra Browser
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("===> ImageServlet Error: " + e.getMessage());
        }
    }
}