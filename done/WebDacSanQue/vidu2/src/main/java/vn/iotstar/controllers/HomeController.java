package vn.iotstar.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.daos.DAO; 
import vn.iotstar.entity.Product; 


@WebServlet("/home")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. Khởi tạo DAO
        DAO dao = new DAO();
        
        // 2. Lấy danh sách sản phẩm
        List<Product> listP = dao.getAllProduct();
        
        // 3. Đặt dữ liệu vào request với tên "listP"
        req.setAttribute("listP", listP); 
        
        // 4. Chuyển tiếp tới view home.jsp
		RequestDispatcher rd = req.getRequestDispatcher("/views/web/home.jsp");
		rd.forward(req, resp);

	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//bất 01 tham số
				String name = req.getParameter("ten");

				String lstname = req.getParameter("holot");

				resp.setContentType("text/html");

				PrintWriter out = resp.getWriter();

				out.println("Hello " + lstname + " " + name);
				out.close();

	}

}
