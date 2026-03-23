package com.example.tuantc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat; // Thêm import này
import java.util.Date;             // Thêm import này
import java.util.Locale;           // Thêm import này

public class CartActivity extends AppCompatActivity {
    ListView lvCart;
    TextView txtTotalPrice, txtSubtotal, txtTax;
    Button btnCheckout;
    ProductAdapter adapter;
    ImageView btnBackCart;

    // Danh sách giỏ hàng hiện tại
    public static List<Product> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        lvCart = findViewById(R.id.lvCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtTax = findViewById(R.id.txtTax);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBackCart = findViewById(R.id.btnBackCart);

        // Khởi tạo adapter (isCart = true, isAdmin = false)
        adapter = new ProductAdapter(this, cartList, true, false);
        lvCart.setAdapter(adapter);

        if (btnBackCart != null) {
            btnBackCart.setOnClickListener(v -> finish());
        }

        btnCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            showAddressBottomSheet();
        });

        updateTotal();
    }

    private void showAddressBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_address, null);
        bottomSheetDialog.setContentView(view);

        EditText edtName = view.findViewById(R.id.edtReceiverName);
        EditText edtPhone = view.findViewById(R.id.edtReceiverPhone);
        EditText edtAddress = view.findViewById(R.id.edtReceiverAddress);
        Button btnConfirm = view.findViewById(R.id.btnConfirmOrder);

        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin nhận hàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            processOrder(name, phone, address);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void processOrder(String name, String phone, String address) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = "tuantc@vimenstore.com";

        // 1. Tạo mã đơn hàng duy nhất
        String orderId = "VM" + System.currentTimeMillis();

        // 2. Định dạng ngày tháng thành String
        String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        // 3. Chuyển đổi danh sách Product sang CartItem
        List<CartItem> itemsInOrder = new ArrayList<>();
        for (Product p : cartList) {
            String firstImg = (p.getImages() != null && !p.getImages().isEmpty()) ? p.getImages().get(0) : "";

            CartItem item = new CartItem(
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    firstImg,
                    p.getSize(),
                    p.getQuantity() > 0 ? p.getQuantity() : 1
            );
            itemsInOrder.add(item);
        }

        // 4. Khởi tạo đối tượng OrderModel
        OrderModel order = new OrderModel(
                orderId,
                name,
                phone,
                address,
                txtTotalPrice.getText().toString(),
                "Đang giao",
                currentTime,
                itemsInOrder
        );

        // 5. Lưu lên Firestore
        db.collection("Orders").document(userEmail)
                .collection("user_orders")
                .document(orderId)
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                    cartList.clear();
                    adapter.notifyDataSetChanged();
                    updateTotal();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi đặt hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void updateTotal() {
        long subtotal = 0;
        for (Product p : cartList) {
            try {
                String priceStr = p.getPrice().replaceAll("[^0-9]", "");
                long unitPrice = Long.parseLong(priceStr);
                int qty = p.getQuantity() > 0 ? p.getQuantity() : 1;
                subtotal += (unitPrice * qty);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        double tax = subtotal * 0.05;
        double total = subtotal + tax;

        if (txtSubtotal != null) txtSubtotal.setText(String.format("%,d VNĐ", subtotal));
        if (txtTax != null) txtTax.setText(String.format("%,.0f VNĐ", tax));
        if (txtTotalPrice != null) txtTotalPrice.setText(String.format("%,.0f VNĐ", total));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            updateTotal();
        }
    }
}