package com.example.tuantc;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    ListView lvCart;
    TextView txtTotalPrice, txtSubtotal, txtTax;
    Button btnCheckout;
    ProductAdapter adapter;
    ImageView btnBackCart;

    // --- KHỞI TẠO DATABASE HELPER ---
    DatabaseHelper dbHelper;

    public static List<Product> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // --- KHỞI TẠO SQLITE ---
        dbHelper = new DatabaseHelper(this);

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "default_user");

        // --- ĐỌC DỮ LIỆU TỪ SQLITE ---
        cartList = new ArrayList<>(dbHelper.getCartList(currentUser));

        lvCart = findViewById(R.id.lvCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtTax = findViewById(R.id.txtTax);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBackCart = findViewById(R.id.btnBackCart);

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
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "tuantc@vimenstore.com");

        String orderId = "VM" + System.currentTimeMillis();
        String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        List<CartItem> itemsInOrder = new ArrayList<>();
        for (Product p : cartList) {
            String firstImg = (p.getImages() != null && !p.getImages().isEmpty()) ? p.getImages().get(0) : "";
            CartItem item = new CartItem(p.getId(), p.getName(), p.getPrice(), firstImg, p.getSize(), p.getQuantity() > 0 ? p.getQuantity() : 1);
            itemsInOrder.add(item);
        }

        OrderModel order = new OrderModel(orderId, name, phone, address, txtTotalPrice.getText().toString(), "Đang giao", currentTime, itemsInOrder);

        db.collection("Orders").document(currentUser)
                .collection("user_orders")
                .document(orderId)
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();

                    // --- XÓA GIỎ HÀNG TRONG SQLITE KHI ĐẶT XONG ---
                    cartList.clear();
                    dbHelper.clearCart(currentUser);

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
            } catch (Exception e) { e.printStackTrace(); }
        }

        double tax = subtotal * 0.05;
        double total = subtotal + tax;

        if (txtSubtotal != null) txtSubtotal.setText(String.format("%,d VNĐ", subtotal));
        if (txtTax != null) txtTax.setText(String.format("%,.0f VNĐ", tax));
        if (txtTotalPrice != null) txtTotalPrice.setText(String.format("%,.0f VNĐ", total));

        // --- LƯU LẠI VÀO SQLITE KHI CÓ THAY ĐỔI ---
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "default_user");
        if (dbHelper != null) dbHelper.saveCartList(currentUser, cartList);
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