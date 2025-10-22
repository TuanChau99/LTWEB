package vn.iotstar.controllers.admin;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/home" })
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Kiểm tra xem servlet có chạy tới đây không  System.out.println(">>> HomeController.doGet() called");

        RequestDispatcher rd = req.getRequestDispatcher("/views/admin/home.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String hoLot = req.getParameter("holot");
        String ten = req.getParameter("ten");

        String fullName = (hoLot != null ? hoLot : "") + " " + (ten != null ? ten : "");

        req.setAttribute("name", fullName.trim());

        System.out.println(">>> doPost received: " + fullName);

        RequestDispatcher rd = req.getRequestDispatcher("/views/admin/home.jsp");
        rd.forward(req, resp);
    }
}
