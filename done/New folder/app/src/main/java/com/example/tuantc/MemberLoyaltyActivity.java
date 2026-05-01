package com.example.tuantc;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MemberLoyaltyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_loyalty);

        ImageView btnBack = findViewById(R.id.btnBackLoyalty);
        TextView tvUserName = findViewById(R.id.tvUserLoyaltyName);
        TextView tvRank = findViewById(R.id.tvCurrentRank);
        TextView tvProgress = findViewById(R.id.tvProgressInfo);
        ProgressBar progressBar = findViewById(R.id.loyaltyProgress);

        // Nhận dữ liệu từ Intent (Gửi từ Profile sang)
        String name = getIntent().getStringExtra("user_name");
        if (name != null) {
            tvUserName.setText(name.toUpperCase());
        }

        int totalSpent = 1500000; // Khách đã tiêu 1.5tr
        int targetSpent = 2000000; // Mục tiêu 2tr để lên hạng Vàng

        if (totalSpent < targetSpent) {
            int remaining = targetSpent - totalSpent;
            tvProgress.setText("Còn " + String.format("%,d", remaining) + "đ nữa để lên hạng Vàng");
            progressBar.setProgress((totalSpent * 100) / targetSpent);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}