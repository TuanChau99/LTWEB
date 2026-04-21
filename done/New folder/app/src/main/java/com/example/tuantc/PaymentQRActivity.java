package com.example.tuantc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentQRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_qr);

        TextView txtAmount = findViewById(R.id.txtAmountQR);
        Button btnDone = findViewById(R.id.btnDoneQR);
        TextView btnCancel = findViewById(R.id.btnCancelQR);

        String amount = getIntent().getStringExtra("amount");
        txtAmount.setText(amount + " VNĐ");

        btnDone.setOnClickListener(v -> {
            Toast.makeText(this, "Hệ thống đang xác nhận thanh toán...", Toast.LENGTH_LONG).show();

            // Xóa giỏ hàng và về trang lịch sử hoặc Home
            CartActivity.cartList.clear();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}