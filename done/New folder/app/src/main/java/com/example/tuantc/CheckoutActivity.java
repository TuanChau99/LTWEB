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
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin nhận hàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            String cleanAmount = totalAmount.replaceAll("[^\\d]", "");
            int selectedId = rgPayment.getCheckedRadioButtonId();

            if (selectedId == R.id.rbQR) {
                Intent intent = new Intent(this, PaymentQRActivity.class);
                intent.putExtra("amount", totalAmount);
                startActivity(intent);
            }
            else if (selectedId == R.id.rbVNPAY) {
                Intent intent = new Intent(this, PaymentVNPAYActivity.class);
                intent.putExtra("amount", cleanAmount);
                startActivityForResult(intent, 999);
            }
            else {
                saveOrderToFirestore(name, phone, address, "Thanh toán khi nhận hàng (COD)");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            // Nếu VNPAY trả về OK, lưu vào Firebase với trạng thái đã thanh toán
            saveOrderToFirestore(name, phone, address, "VNPAY (Đã thanh toán)");
        }
    }

    private void saveOrderToFirestore(String name, String phone, String address, String paymentMethod) {
        String dateOrder = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String userEmail = "tuantest@gmail.com";

        List<Map<String, Object>> itemsList = new ArrayList<>();
        for (Product p : CartActivity.cartList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("productId", p.getId());
            itemMap.put("name", p.getName());
            itemMap.put("price", p.getPrice());
            itemMap.put("quantity", p.getQuantity() > 0 ? p.getQuantity() : 1);
            String img = (p.getImages() != null && !p.getImages().isEmpty()) ? p.getImages().get(0) : "";
            itemMap.put("image", img);
            itemsList.add(itemMap);
        }

        String orderId = "VM" + System.currentTimeMillis();
        String finalStatus = paymentMethod.contains("VNPAY") ? "Đã Thanh Toán" : "Đang Chờ Duyệt";

        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("customerName", name);
        order.put("customerPhone", phone);
        order.put("shippingAddress", address);
        order.put("totalPrice", totalAmount);
        order.put("paymentMethod", paymentMethod);
        order.put("status", finalStatus);
        order.put("dateOrder", dateOrder);
        order.put("customerEmail", userEmail);
        order.put("items", itemsList);

        db.collection("Orders").document(userEmail)
                .collection("user_orders").document(orderId)
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    // Lưu thêm một bản sao vào collection Orders tổng để Admin dễ quản lý
                    db.collection("Orders").document(orderId).set(order);
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                    CartActivity.cartList.clear();
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                });
    }
}