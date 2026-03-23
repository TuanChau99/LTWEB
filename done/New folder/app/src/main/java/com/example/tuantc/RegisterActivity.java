package com.example.tuantc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName, edtEmail, edtPhone, edtPass;
    Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);

        edtName = findViewById(R.id.edtRegFullName);
        edtEmail = findViewById(R.id.edtRegEmail);
        edtPhone = findViewById(R.id.edtRegPhone);
        edtPass = findViewById(R.id.edtRegPass);
        btnReg = findViewById(R.id.btnRegister);

        btnReg.setOnClickListener(v -> {
            SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("saved_name", edtName.getText().toString());
            editor.putString("saved_email", edtEmail.getText().toString());
            editor.putString("saved_phone", edtPhone.getText().toString());
            editor.putString("saved_pass", edtPass.getText().toString());
            editor.apply();

            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}