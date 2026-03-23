package com.example.tuantc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProfileActivity extends AppCompatActivity {

    ImageView btnBackProfile;
    CardView btnGoToOrders;
    LinearLayout btnWaitingConfirm, btnWaitingPick, btnShipping, btnRating;

    // 1. Thêm biến để kiểm tra quyền Admin
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);

        // Ánh xạ View
        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvEmail = findViewById(R.id.tvProfileEmail);
        Button btnLogout = findViewById(R.id.btnLogout);
        btnBackProfile = findViewById(R.id.btnBackProfile);
        btnGoToOrders = findViewById(R.id.btnGoToOrders);
        btnWaitingConfirm = findViewById(R.id.btnWaitingConfirm);
        btnShipping = findViewById(R.id.btnShipping);

        // 2. LOGIC KIỂM TRA QUYỀN:
        // kiểm tra tên người dùng hiện tại, nếu là "Admin" thì set isAdmin = true
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "Người dùng");

        if (currentUser.equalsIgnoreCase("Admin")) {
            isAdmin = true;
        }

        tvName.setText(currentUser);
        tvEmail.setText(currentUser.toLowerCase().replace(" ", "") + "@vimenstore.com");

        // --- SỰ KIỆN CLICK ---

        // Mở Tab "Tất cả" (vị trí 0)
        btnGoToOrders.setOnClickListener(v -> openOrderHistory(0));

        // Mở Tab "Tất cả"
        if (btnWaitingConfirm != null) {
            btnWaitingConfirm.setOnClickListener(v -> openOrderHistory(0));
        }

        // Mở Tab "Đang giao" (vị trí 1)
        if (btnShipping != null) {
            btnShipping.setOnClickListener(v -> openOrderHistory(1));
        }

        if (btnBackProfile != null) {
            btnBackProfile.setOnClickListener(v -> finish());
        }

        btnLogout.setOnClickListener(v -> showLogoutConfirmation(pref));
    }

    // 3.  Truyền thêm isAdmin qua Intent
    private void openOrderHistory(int tabPosition) {
        Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
        intent.putExtra("selected_tab", tabPosition);
        intent.putExtra("isAdmin", isAdmin); // DÒNG QUAN TRỌNG NHẤT: Gửi quyền Admin đi
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