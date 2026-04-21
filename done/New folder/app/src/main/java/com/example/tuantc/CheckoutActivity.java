package com.example.tuantc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    EditText edtName, edtPhone, edtAddress;
    RadioGroup rgPayment;
    TextView txtTotal;
    Button btnConfirm;
    ImageView btnBack;
    String totalAmount;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();

        edtName = findViewById(R.id.edtReceiverName);
        edtPhone = findViewById(R.id.edtReceiverPhone);
        edtAddress = findViewById(R.id.edtReceiverAddress);
        rgPayment = findViewById(R.id.rgPaymentMethod);
        txtTotal = findViewById(R.id.txtTotalCheckout);
        btnConfirm = findViewById(R.id.btnConfirmOrder);
        btnBack = findViewById(R.id.btnBackCheckout);

        totalAmount = getIntent().getStringExtra("total_amount");
        txtTotal.setText(totalAmount + " VNĐ");

        btnBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = rgPayment.getCheckedRadioButtonId();
            if (selectedId == R.id.rbQR) {
                Intent intent = new Intent(this, PaymentQRActivity.class);
                intent.putExtra("amount", totalAmount);
                startActivity(intent);
            } else {
                saveOrderToFirestore(name, phone, address);
            }
        });
    }

    private void saveOrderToFirestore(String name, String phone, String address) {
        String dateOrder = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String userEmail = "tuantc@vimenstore.com";

        // 1. Chuyển đổi CartActivity.cartList sang danh sách CartItem để lưu mảng items
        List<Map<String, Object>> itemsList = new ArrayList<>();
        for (Product p : CartActivity.cartList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("productId", p.getId());
            itemMap.put("name", p.getName());
            itemMap.put("price", p.getPrice());
            itemMap.put("quantity", p.getQuantity() > 0 ? p.getQuantity() : 1);

            // Lấy hình ảnh đầu tiên để hiển thị ở trang chi tiết
            String img = (p.getImages() != null && !p.getImages().isEmpty()) ? p.getImages().get(0) : "";
            itemMap.put("image", img);

            itemsList.add(itemMap);
        }

        // 2. Tạo ID đơn hàng duy nhất
        String orderId = "VM" + System.currentTimeMillis();

        // 3. Đóng gói dữ liệu
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("customerName", name);
        order.put("customerPhone", phone);
        order.put("shippingAddress", address);
        order.put("totalPrice", totalAmount);
        order.put("status", "Đang Chờ Duyệt");
        order.put("dateOrder", dateOrder);
        order.put("customerEmail", userEmail);
        order.put("items", itemsList); // LƯU MẢNG SẢN PHẨM Ở ĐÂY

        // 4. Lưu vào đúng cấu trúc phân cấp để hiển thị hình ảnh
        db.collection("Orders").document(userEmail)
                .collection("user_orders").document(orderId)
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    // Lưu dự phòng tại root Orders để dễ quản lý
                    db.collection("Orders").document(orderId).set(order);

                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                    CartActivity.cartList.clear();

                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi đặt hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}