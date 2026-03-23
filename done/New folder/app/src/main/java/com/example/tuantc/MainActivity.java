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

        // Ánh xạ các View từ XML
        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtGoToRegister = findViewById(R.id.txtGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            // Lấy thông tin từ SharedPreferences (nếu người dùng đã đăng ký trước đó)
            SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String sEmail = pref.getString("saved_email", "");
            String sPhone = pref.getString("saved_phone", "");
            String sPass = pref.getString("saved_pass", "");

            // --- KIỂM TRA ĐIỀU KIỆN ĐĂNG NHẬP ---
            // 1. Kiểm tra tài khoản đã đăng ký (Email hoặc SĐT)
            boolean isRegisteredUser = ((user.equals(sEmail) || user.equals(sPhone)) && pass.equals(sPass) && !sPass.isEmpty());

            // 2. Tài khoản ADMIN mặc định (Có quyền quản trị)
            boolean isAdminLogin = (user.equals("admin") && pass.equals("123"));

            // 3. Tài khoản USER TEST mặc định (Dành cho việc test nhanh giao diện khách hàng)
            boolean isTestUserLogin = (user.equals("user") && pass.equals("123"));

            if (isRegisteredUser || isAdminLogin || isTestUserLogin) {
                // Lưu thông tin người dùng hiện tại vào bộ nhớ tạm
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("current_user", user);
                editor.apply();

                // Chuyển sang màn hình Home
                Intent intent = new Intent(this, HomeActivity.class);

                // PHÂN QUYỀN: Kiểm tra nếu là admin thì mới cấp quyền Admin
                if (user.equals("admin") || user.equals("admin@gmail.com")) {
                    intent.putExtra("isAdmin", true);
                } else {
                    // user1 hoặc các user đã đăng ký khác sẽ là false
                    intent.putExtra("isAdmin", false);
                }

                startActivity(intent);
                finish(); // Đóng màn hình Login sau khi đăng nhập thành công

            } else {
                Toast.makeText(this, "Thông tin đăng nhập không chính xác!", Toast.LENGTH_SHORT).show();
            }
        });

        // Chuyển sang màn hình đăng ký
        txtGoToRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}