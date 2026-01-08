package vn.iotstar.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.configs.VnPayConfig;

@WebServlet("/vnpay-payment")
public class VnPayController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lấy ID thật từ CheckoutControl truyền sang
            String orderId = req.getParameter("orderId");
            String amountParam = req.getParameter("amount");
            
            System.out.println("VnPayController đang xử lý đơn hàng ID: " + orderId);
            
            if (amountParam == null || amountParam.isEmpty()) amountParam = "0";
            long amount = (long) (Double.parseDouble(amountParam) * 100L);

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            
            // Gắn mã ngẫu nhiên để tránh lỗi trùng mã giao dịch khi thanh toán lại
            vnp_Params.put("vnp_TxnRef", orderId + "_" + VnPayConfig.getRandomNumber(4)); 
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_Returnurl);
            vnp_Params.put("vnp_IpAddr", VnPayConfig.getIpAddress(req));

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime()));

            // Sắp xếp và băm dữ liệu
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                         .append('=')
                         .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            
            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
            
            resp.sendRedirect(paymentUrl);
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi tạo link thanh toán: " + e.getMessage());
        }
    }
}