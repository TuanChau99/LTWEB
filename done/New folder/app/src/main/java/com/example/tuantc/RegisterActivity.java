package com.example.tuantc;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName, edtEmail, edtPhone, edtPass;
    Button btnReg;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);

        db = FirebaseFirestore.getInstance();

        edtName = findViewById(R.id.edtRegFullName);
        edtEmail = findViewById(R.id.edtRegEmail);
        edtPhone = findViewById(R.id.edtRegPhone);
        edtPass = findViewById(R.id.edtRegPass);
        btnReg = findViewById(R.id.btnRegister);

        btnReg.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo Object để lưu lên Firebase Users Collection
            // Sử dụng email làm username để đăng nhập đồng bộ với MainActivity
            Map<String, Object> user = new HashMap<>();
            user.put("username", email);
            user.put("password", pass);
            user.put("fullName", name);
            user.put("phone", phone);
            user.put("rules", "user"); // Mặc định là user khi đăng ký mới

            db.collection("Users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}