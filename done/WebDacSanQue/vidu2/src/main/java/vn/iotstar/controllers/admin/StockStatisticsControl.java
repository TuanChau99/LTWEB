package vn.iotstar.controllers.admin;

import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.daos.ProductDAO;
import vn.iotstar.daos.impl.ProductDaoImpl;
import vn.iotstar.entity.Product;

@WebServlet(urlPatterns = {"/admin/stockStats"})
public class StockStatisticsControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO dao = new ProductDaoImpl();
        List<Product> allProduct = dao.getAll();
        List<Product> lowStock = dao.getLowStock();

        // Tính tổng số lượng hàng trong kho
        int totalStock = 0;
        double totalValue = 0;
        for (Product p : allProduct) {
            totalStock += p.getStock();
            totalValue += (p.getStock() * p.getPrice());
        }

        request.setAttribute("totalStock", totalStock);
        request.setAttribute("totalValue", totalValue);
        request.setAttribute("lowStockList", lowStock);
        request.setAttribute("allProduct", allProduct);
        
        request.getRequestDispatcher("/views/admin/stock-stats.jsp").forward(request, response);
    }
}