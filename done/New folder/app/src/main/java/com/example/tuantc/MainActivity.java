package com.example.tuantc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText edtUser, edtPass;
    Button btnLogin;
    TextView txtGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtGoToRegister = findViewById(R.id.txtGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String sEmail = pref.getString("saved_email", "");
            String sPhone = pref.getString("saved_phone", "");
            String sPass = pref.getString("saved_pass", "");

            boolean isRegisteredUser = ((user.equals(sEmail) || user.equals(sPhone)) && pass.equals(sPass) && !sPass.isEmpty());
            boolean isAdminLogin = (user.equals("admin") && pass.equals("123"));
            boolean isTestUserLogin = (user.equals("user") && pass.equals("123"));

            if (isRegisteredUser || isAdminLogin || isTestUserLogin) {
                // Xác định quyền Admin
                boolean isAdmin = (user.equals("admin") || user.equals("admin@gmail.com"));

                // Lưu thông tin vào SharedPreferences để dùng cho toàn hệ thống
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("current_user", user); // Đây chính là userKey để lưu giỏ hàng riêng
                editor.putBoolean("is_admin_role", isAdmin);
                editor.apply();

                // Chuyển sang màn hình Home
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("isAdmin", isAdmin);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Thông tin đăng nhập không chính xác!", Toast.LENGTH_SHORT).show();
            }
        });

        txtGoToRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}