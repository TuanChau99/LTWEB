package vn.iotstar.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.daos.DAO;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.Category;

//@WebServlet("/product")
public class ProductListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int PRODUCTS_PER_PAGE = 9;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DAO dao = new DAO();

		// --- 1. LẤY VÀ CHUẨN HÓA THAM SỐ LỌC/SẮP XẾP/PHÂN TRANG ---
		String categoryId = request.getParameter("cid");
		String sortBy = request.getParameter("sort");
		String minPrice = request.getParameter("min_price");
		String maxPrice = request.getParameter("max_price");

		String pageStr = request.getParameter("page");
		// currentPage ban đầu nhận giá trị từ tham số URL (vd: 1 hoặc 2)
		int currentPage = (pageStr == null || pageStr.isEmpty()) ? 1 : Integer.parseInt(pageStr);

		String isLoadMore = request.getParameter("loadMore");

		List<Product> baseList;
		int activeTag = 0;

		// --- 2 & 3: Lọc, Sắp xếp (GIỮ NGUYÊN) ---

		if (categoryId != null && !categoryId.isEmpty()) {
			try {
				int cid = Integer.parseInt(categoryId);
				baseList = dao.getProductByCID(cid);
				activeTag = cid;
			} catch (NumberFormatException e) {
				baseList = dao.getAllProduct();
			}
		} else {
			baseList = dao.getAllProduct();
		}

		List<Product> filteredAndSortedList = applySortAndFilter(baseList, sortBy, minPrice, maxPrice);

		// --- 4. ÁP DỤNG PHÂN TRANG (PAGINATION) ---

		int totalProducts = filteredAndSortedList.size();
		int endPage = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);

		// *** LOGIC CHUẨN HÓA TRANG ĐỂ TRÁNH LỖI PHÂN TRANG TRUYỀN THỐNG ***
		// CHỈ áp dụng chuẩn hóa (vd: vượt quá endPage thì về endPage) cho request
		// truyền thống.
		// Với LoadMore, ta tin tưởng số trang JS gửi lên.
		if (!"true".equals(isLoadMore)) {
			if (currentPage < 1) {
				currentPage = 1;
			} else if (currentPage > endPage && endPage > 0) {
				currentPage = endPage;
			}
		}
		
		// Tính toán chỉ mục (Đảm bảo logic này hoạt động đúng với currentPage = 2)
		int startIndex = (currentPage - 1) * PRODUCTS_PER_PAGE;
		int endIndex = Math.min(startIndex + PRODUCTS_PER_PAGE, totalProducts);

		List<Product> pagedList = new ArrayList<>();
		if (startIndex < endIndex) {
			pagedList = filteredAndSortedList.subList(startIndex, endIndex);
		}

		// --- 5. LẤY DỮ LIỆU PHỤ & ĐẶT ATTRIBUTE (GIỮ NGUYÊN) ---

		List<Category> listCC = dao.getAllCategory();
		Product lastProduct = dao.getLastProduct();

		request.setAttribute("listP", pagedList); 
		request.setAttribute("listCC", listCC);
		request.setAttribute("p", lastProduct);
		request.setAttribute("tag", currentPage); 
		request.setAttribute("endP", endPage); 
		request.setAttribute("activeTag", activeTag);

		// --- 6. FORWARD: QUYẾT ĐỊNH FORWARD DỰA TRÊN loadMore ---
		if ("true".equals(isLoadMore)) {
			// CHỈ trả về danh sách sản phẩm nếu là yêu cầu LoadMore AJAX
			request.getRequestDispatcher("/views/web/product_ajax_list.jsp").forward(request, response);
		} else {
			// Tải toàn bộ trang cho lần đầu hoặc phân trang truyền thống
			request.getRequestDispatcher("/views/web/product.jsp").forward(request, response);
		}
	}

	// Phương thức applySortAndFilter giữ nguyên
	private List<Product> applySortAndFilter(List<Product> products, String sortBy, String minPriceString,
			String maxPriceString) {
		// ... (Logic giữ nguyên)
		List<Product> resultList = new ArrayList<>(products);

		// 1. ÁP DỤNG LỌC KHOẢNG GIÁ (FILTER)
		try {
			if (minPriceString != null || maxPriceString != null) {

				double minP = (minPriceString != null && !minPriceString.isEmpty()) ? Double.parseDouble(minPriceString)
						: 0;
				double maxP = (maxPriceString != null && !maxPriceString.isEmpty()) ? Double.parseDouble(maxPriceString)
						: Double.MAX_VALUE;

				resultList = resultList.stream().filter(p -> p.getPrice() >= minP && p.getPrice() <= maxP)
						.collect(Collectors.toList());
			}
		} catch (NumberFormatException ignored) {
		}

		// 2. ÁP DỤNG SẮP XẾP (SORT)
		if (sortBy != null && !sortBy.isEmpty() && !sortBy.equals("default")) {
			switch (sortBy) {
			case "price_asc":
				Collections.sort(resultList, Comparator.comparingDouble(Product::getPrice));
				break;
			case "price_desc":
				Collections.sort(resultList, Comparator.comparingDouble(Product::getPrice).reversed());
				break;
			case "name_asc":
				Collections.sort(resultList, Comparator.comparing(Product::getName));
				break;
			}
		}
		return resultList;
	}
}