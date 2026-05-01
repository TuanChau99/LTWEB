package com.example.tuantc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Ánh xạ tập trung
        setupClickListeners();

        findViewById(R.id.btnLogoutAdmin).setOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        // Danh sách ID CardView
        int[] cardIds = {
                R.id.cardManageProduct, R.id.cardManageCategory, R.id.cardManageOrder,
                R.id.cardReport, R.id.cardManageUser, R.id.cardManageVoucher,
                R.id.cardLoyalty, R.id.cardSwitchUser
        };

        for (int id : cardIds) {
            View card = findViewById(id);
            if (card != null) {
                card.setOnClickListener(this::handleCardClick);
            }
        }
    }

    private void handleCardClick(View v) {
        int id = v.getId();
        Intent intent = null;

        if (id == R.id.cardManageProduct) {
            intent = new Intent(this, AdminProductActivity.class);
        } else if (id == R.id.cardManageCategory) {
            intent = new Intent(this, AdminCategoryActivity.class);
        } else if (id == R.id.cardManageOrder) {
            intent = new Intent(this, OrderHistoryActivity.class);
            intent.putExtra("isAdmin", true);
        } else if (id == R.id.cardReport) {
            intent = new Intent(this, AdminStatsActivity.class);
        } else if (id == R.id.cardManageUser) {
            intent = new Intent(this, AdminUserActivity.class);
        } else if (id == R.id.cardManageVoucher) {
            intent = new Intent(this, VoucherListActivity.class);
            intent.putExtra("isAdmin", true);
        } else if (id == R.id.cardLoyalty) {
            intent = new Intent(this, AdminLoyaltyActivity.class);
        } else if (id == R.id.cardSwitchUser) {
            intent = new Intent(this, HomeActivity.class); // Hoặc MainActivity tùy shop bạn
            intent.putExtra("isAdmin", false);
        }

        if (intent != null) startActivity(intent);
    }
}