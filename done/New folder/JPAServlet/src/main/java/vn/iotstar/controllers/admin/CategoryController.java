package vn.iotstar.controllers.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Category;
import vn.iotstar.services.CategoryService;
import vn.iotstar.services.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = {"/admin/categories"})
public class CategoryController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	CategoryService cateService = new CategoryServiceImpl();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<Category> list = cateService.findAll();
		req.setAttribute("listcate", list);
		RequestDispatcher rd = req.getRequestDispatcher("/views/admin/category-list.jsp");
		rd.forward(req, resp);
	}

}
