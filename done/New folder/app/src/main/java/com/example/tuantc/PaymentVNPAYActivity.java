package com.example.tuantc;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class PaymentVNPAYActivity extends AppCompatActivity {
    private WebView webView;

    // Thông tin cấu hình từ Sandbox
    private String vnp_TmnCode = "D104RD6A";
    private String vnp_HashSecret = "RL8OMRWHM9T00GR6VBCHKFQINSASNKE2";
    private String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        // User Agent giúp WebView hiển thị giao diện thanh toán
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Mobile Safari/537.36");

        String amount = getIntent().getStringExtra("amount");
        String vnpayUrl = buildVNPAYUrl(amount);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d("VNPAY_URL", "Loading URL: " + url);

                // Kiểm tra mã kết quả trả về từ VNPAY
                if (url.contains("vnp_ResponseCode=00")) {
                    Toast.makeText(PaymentVNPAYActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                    return true;
                } else if (url.contains("vnp_ResponseCode")) {
                    Toast.makeText(PaymentVNPAYActivity.this, "Giao dịch không thành công.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webView.loadUrl(vnpayUrl);
    }

    private String buildVNPAYUrl(String amount) {
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", amount + "00"); // Nhân 100 theo quy định VNPAY
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
        vnp_Params.put("vnp_OrderInfo", "Thanhtoandonhang");
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "https://webhook.site/vnpay_return");
        vnp_Params.put("vnp_IpAddr", getMobileIPAddress()); // Lấy IP tự động
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // 1. Sắp xếp tham số theo alphabet (bắt buộc)
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        // 2. Xây dựng chuỗi hash và query
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);

            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                try {
                    // Encode cả Key và Value theo chuẩn US_ASCII
                    String encodedKey = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
                    String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());

                    // 1. Xây dựng Query gửi đi
                    query.append(encodedKey).append('=').append(encodedValue);

                    // 2. Xây dựng Data để băm (Phiên bản 2.1.0 yêu cầu băm trên chuỗi đã Encode)
                    hashData.append(encodedKey).append('=').append(encodedValue);

                    if (i < fieldNames.size() - 1) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }

        // 3. Tạo SecureHash
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());

        // 4. URL cuối cùng hoàn chỉnh
        String finalUrl = vnp_Url + "?" + query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;

        Log.d("VNPAY_DEBUG", "Final HashData: " + hashData.toString());
        return finalUrl;
    }

    // Hàm lấy địa chỉ IP của thiết bị
    public String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (isIPv4) return sAddr;
                    }
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return "14.161.1.1"; // IP dự phòng nếu không lấy được
    }

    // Hàm băm bảo mật HMAC-SHA512
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) return "";
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            final SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) { return ""; }
    }
}