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
    CardView btnGoToOrders, btnAboutUs, cardAdminSection;
    RelativeLayout btnAdminStats;
    LinearLayout btnWaitingConfirm, btnShipping, btnKhoVoucher, btnGoToCart; // Thêm btnGoToCart
    TextView tvName, tvEmail, tvMemberRank;
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);

        // Ánh xạ View
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvMemberRank = findViewById(R.id.tvMemberRank);
        Button btnLogout = findViewById(R.id.btnLogout);
        btnBackProfile = findViewById(R.id.btnBackProfile);
        btnGoToOrders = findViewById(R.id.btnGoToOrders);
        btnAboutUs = findViewById(R.id.btnAboutUs);
        btnWaitingConfirm = findViewById(R.id.btnWaitingConfirm);
        btnShipping = findViewById(R.id.btnShipping);

        // Nút Giỏ hàng mới thêm
        btnGoToCart = findViewById(R.id.btnGoToCart);
        btnKhoVoucher = findViewById(R.id.btnKhoVoucher);

        // View cho Admin
        cardAdminSection = findViewById(R.id.cardAdminSection);
        btnAdminStats = findViewById(R.id.btnAdminStats);

        // KIỂM TRA QUYỀN TRUY CẬP
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "Người dùng");

        if (currentUser.equalsIgnoreCase("Admin")) {
            isAdmin = true;
            cardAdminSection.setVisibility(View.VISIBLE);
            tvMemberRank.setText("Quản trị viên");
        }

        tvName.setText(currentUser);
        tvEmail.setText(currentUser.toLowerCase().replace(" ", "") + "@vimenstore.com");

        // --- SỰ KIỆN CLICK ---

        // Mở Giỏ hàng
        btnGoToCart.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, CartActivity.class));
        });

        // Mở Kho Voucher
        btnKhoVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, VoucherListActivity.class);
            startActivity(intent);
        });

        // Nút Thống kê
        btnAdminStats.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, AdminStatsActivity.class));
        });

        btnAboutUs.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, AboutUsActivity.class));
        });

        btnGoToOrders.setOnClickListener(v -> openOrderHistory(0));
        btnWaitingConfirm.setOnClickListener(v -> openOrderHistory(0));
        btnShipping.setOnClickListener(v -> openOrderHistory(1));

        btnBackProfile.setOnClickListener(v -> finish());
        btnLogout.setOnClickListener(v -> showLogoutConfirmation(pref));
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
                .setMessage("Bạn có chắc chắn muốn thoát tài khoản không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("current_user");
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