package com.example.tuantc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProfileActivity extends AppCompatActivity {

    ImageView btnBackProfile;
    CardView btnGoToOrders, btnMemberLoyalty; // Thêm btnMemberLoyalty
    RelativeLayout btnWarranty, btnAboutUs;
    LinearLayout btnWaitingConfirm, btnShipping, btnKhoVoucher, btnGoToCart;
    TextView tvName, tvEmail, tvMemberRank;
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);

        // 1. Ánh xạ thông tin cá nhân
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvMemberRank = findViewById(R.id.tvMemberRank);
        btnBackProfile = findViewById(R.id.btnBackProfile);
        Button btnLogout = findViewById(R.id.btnLogout);

        // 2. Ánh xạ các nút CardView/RelativeLayout
        btnGoToOrders = findViewById(R.id.btnGoToOrders);
        btnMemberLoyalty = findViewById(R.id.btnMemberLoyalty); // Ánh xạ mục đặc quyền
        btnWarranty = findViewById(R.id.btnWarranty);
        btnAboutUs = findViewById(R.id.btnAboutUs);

        // 3. Ánh xạ các nút chức năng (LinearLayout)
        btnWaitingConfirm = findViewById(R.id.btnWaitingConfirm);
        btnShipping = findViewById(R.id.btnShipping);
        btnGoToCart = findViewById(R.id.btnGoToCart);
        btnKhoVoucher = findViewById(R.id.btnKhoVoucher);

        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "Khách");
        isAdmin = pref.getBoolean("is_admin_role", false);

        tvName.setText(currentUser);
        tvEmail.setText(currentUser.toLowerCase().replace(" ", "") + "@vimenstore.com");

        // Hiển thị hạng thành viên dựa trên vai trò
        if (isAdmin) {
            tvMemberRank.setText("Quản trị viên");
            tvMemberRank.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            // Tuấn có thể lấy điểm từ DB để set text cụ thể như "Thành viên Vàng" ở đây
            tvMemberRank.setText("Thành viên Bạc");
        }

        // --- CÀI ĐẶT SỰ KIỆN CLICK ---

        // Đặc quyền thành viên (Dẫn tới màn chi tiết ưu đãi)
        btnMemberLoyalty.setOnClickListener(v -> {
            Intent intent = new Intent(this, MemberLoyaltyActivity.class);
            intent.putExtra("user_name", currentUser);
            startActivity(intent);
        });

        // Chính sách bảo hành
        btnWarranty.setOnClickListener(v -> {
            Intent intent = new Intent(this, PolicyActivity.class);
            intent.putExtra("policy_type", "warranty");
            startActivity(intent);
        });

        // Về chúng tôi
        btnAboutUs.setOnClickListener(v -> startActivity(new Intent(this, AboutUsActivity.class)));

        // Kho Voucher
        btnKhoVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(this, VoucherListActivity.class);
            intent.putExtra("isAdmin", false);
            startActivity(intent);
        });

        btnGoToCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnBackProfile.setOnClickListener(v -> finish());
        btnLogout.setOnClickListener(v -> showLogoutConfirmation(pref));

        // Đơn hàng
        btnGoToOrders.setOnClickListener(v -> openOrderHistory(0));
        btnWaitingConfirm.setOnClickListener(v -> openOrderHistory(0));
        btnShipping.setOnClickListener(v -> openOrderHistory(1));
    }

    private void openOrderHistory(int tabPosition) {
        Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
        intent.putExtra("selected_tab", tabPosition);
        intent.putExtra("isAdmin", isAdmin);
        startActivity(intent);
    }

    private void showLogoutConfirmation(SharedPreferences pref) {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn thoát không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}