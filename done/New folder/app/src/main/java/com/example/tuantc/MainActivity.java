package com.example.tuantc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText edtUser, edtPass;
    private Button btnLogin;
    private TextView txtGoToRegister;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtGoToRegister = findViewById(R.id.txtGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            loginWithFirebase(user, pass);
        });

        txtGoToRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void loginWithFirebase(String username, String password) {
        db.collection("Users")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        String role = document.getString("rules");
                        boolean isAdmin = "admin".equalsIgnoreCase(role);

                        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("current_user", username);
                        editor.putBoolean("is_admin_role", isAdmin);
                        editor.apply();

                        Intent intent;
                        if (isAdmin) {
                            // Nếu là Admin thì vào trang Dashboard quản lý
                            intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                            Toast.makeText(this, "Chào Admin!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Nếu là User thường thì vào trang Home mua sắm
                            intent = new Intent(MainActivity.this, HomeActivity.class);
                            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        }

                        intent.putExtra("isAdmin", isAdmin);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kết nối hệ thống: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}