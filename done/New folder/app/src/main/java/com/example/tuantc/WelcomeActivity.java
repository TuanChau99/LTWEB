package com.example.tuantc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        // Ánh xạ các View từ file XML
        Button btnGetStarted = findViewById(R.id.btnGetStarted);
        TextView btnSignIn = findViewById(R.id.btnSignIn);

        // Xử lý sự kiện khi nhấn "Let's Get Started" -> Vào xem App ngay (Guest Mode)
        btnGetStarted.setOnClickListener(v -> {
            // Lưu một cờ đánh dấu là khách đang vào bằng Guest Mode
            SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            pref.edit().putBoolean("is_guest", true).apply();

            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện khi nhấn "Already have an account? Sign In"
        btnSignIn.setOnClickListener(v -> {
            // Chuyển sang LoginActivity (Trang đăng nhập)
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}