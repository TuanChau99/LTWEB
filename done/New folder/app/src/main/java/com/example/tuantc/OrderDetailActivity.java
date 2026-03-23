package com.example.tuantc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailActivity extends AppCompatActivity {
    TextView tvInfo, tvStatus, tvId;
    ListView lvItems;
    Button btnAction, btnCancel;
    ImageView btnBack;
    FirebaseFirestore db;
    String orderId, userEmail = "tuantc@vimenstore.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        db = FirebaseFirestore.getInstance();
        orderId = getIntent().getStringExtra("orderId");
        boolean isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        // Ánh xạ View
        tvId = findViewById(R.id.tvOrderIdDetail);
        tvInfo = findViewById(R.id.tvDetailInfo);
        tvStatus = findViewById(R.id.tvDetailStatus);
        lvItems = findViewById(R.id.lvOrderItems);
        btnAction = findViewById(R.id.btnAdminConfirm);
        btnCancel = findViewById(R.id.btnCancelOrder);
        btnBack = findViewById(R.id.btnBackDetail);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        loadOrderDetail();

        // Logic hiển thị nút theo quyền Admin
        if (isAdmin) {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("Xác nhận hoàn thành");
            btnAction.setOnClickListener(v -> updateStatus("Hoàn thành"));

            // Admin cũng có thể hủy đơn nếu cần
            btnCancel.setText("Hủy đơn (Admin)");
        } else {
            btnAction.setVisibility(View.GONE);
        }

        btnCancel.setOnClickListener(v -> updateStatus("Đã hủy"));
    }

    private void loadOrderDetail() {
        db.collection("Orders").document(userEmail)
                .collection("user_orders").document(orderId)
                .get().addOnSuccessListener(doc -> {
                    OrderModel order = doc.toObject(OrderModel.class);
                    if (order != null) {
                        if (tvId != null) tvId.setText("Mã đơn: " + order.getOrderId());

                        tvInfo.setText("Người nhận: " + order.getCustomerName() +
                                "\nSĐT: " + order.getCustomerPhone() +
                                "\nĐịa chỉ: " + order.getShippingAddress());

                        tvStatus.setText("Trạng thái: " + order.getStatus());

                        // Hiển thị danh sách sản phẩm trong đơn hàng
                        CartItemAdapter adapter = new CartItemAdapter(this, order.getItems());
                        lvItems.setAdapter(adapter);
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Không thể tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateStatus(String newStatus) {
        db.collection("Orders").document(userEmail)
                .collection("user_orders").document(orderId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đã cập nhật trạng thái: " + newStatus, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}