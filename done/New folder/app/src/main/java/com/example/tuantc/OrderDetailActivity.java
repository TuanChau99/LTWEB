package com.example.tuantc;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {
    TextView tvInfo, tvStatus, tvId;
    ListView lvItems;
    Button btnAction, btnCancel;
    ImageView btnBack;
    FirebaseFirestore db;
    String orderId;
    String userEmail = "tuantc@vimenstore.com";
    DocumentSnapshot currentDoc; // Biến lưu trữ dữ liệu để đổ vào Dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        db = FirebaseFirestore.getInstance();
        orderId = getIntent().getStringExtra("orderId");
        boolean isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        tvId = findViewById(R.id.tvOrderIdDetail);
        tvInfo = findViewById(R.id.tvDetailInfo);
        tvStatus = findViewById(R.id.tvDetailStatus);
        lvItems = findViewById(R.id.lvOrderItems);
        btnAction = findViewById(R.id.btnAdminConfirm);
        btnCancel = findViewById(R.id.btnCancelOrder);
        btnBack = findViewById(R.id.btnBackDetail);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        loadOrderDetail();

        // PHẦN XỬ LÝ GIAO DIỆN ADMIN
        if (isAdmin) {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("Cập nhật đơn hàng");
            btnAction.setOnClickListener(v -> showUpdateOrderDialog());
            btnCancel.setVisibility(View.GONE); // Admin dùng Dialog để hủy/sửa sẽ tốt hơn
        } else {
            btnAction.setVisibility(View.GONE);
            btnCancel.setOnClickListener(v -> updateStatus("Đã hủy"));
        }
    }

    private void loadOrderDetail() {
        if (orderId == null) return;
        db.collection("Orders").document(userEmail).collection("user_orders").document(orderId)
                .get().addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.get("items") != null) {
                        displayOrderData(doc);
                    } else {
                        db.collection("Orders").document(orderId).get().addOnSuccessListener(this::displayOrderData);
                    }
                });
    }

    private void displayOrderData(DocumentSnapshot doc) {
        if (!doc.exists()) return;
        this.currentDoc = doc;

        String name = doc.getString("customerName");
        String phone = doc.getString("customerPhone");
        String address = doc.getString("shippingAddress");
        if (address == null || address.isEmpty()) address = doc.getString("address");
        String status = doc.getString("status");

        tvId.setText("Mã đơn: " + doc.getId());
        tvInfo.setText("Người nhận: " + (name != null ? name : "N/A") +
                "\nSĐT: " + (phone != null ? phone : "N/A") +
                "\nĐịa chỉ: " + (address != null ? address : "N/A"));
        tvStatus.setText("Trạng thái: " + (status != null ? status : "Đang xử lý"));

        List<CartItem> itemList = new ArrayList<>();
        List<Map<String, Object>> itemsRaw = (List<Map<String, Object>>) doc.get("items");
        if (itemsRaw != null) {
            for (Map<String, Object> itemMap : itemsRaw) {
                CartItem item = new CartItem();
                item.setName((String) itemMap.get("name"));
                item.setPrice((String) itemMap.get("price"));
                item.setImage((String) itemMap.get("image"));
                Object q = itemMap.get("quantity");
                item.setQuantity(q != null ? Integer.parseInt(String.valueOf(q)) : 1);
                itemList.add(item);
            }
        }

        if (!itemList.isEmpty()) {
            CartItemAdapter adapter = new CartItemAdapter(this, itemList);
            lvItems.setAdapter(adapter);
            setListViewHeightBasedOnChildren(lvItems);
        }
    }

    private void showUpdateOrderDialog() {
        if (currentDoc == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_update_order, null);
        builder.setView(view);

        EditText edtCustomer = view.findViewById(R.id.edtOrderCustomer);
        EditText edtAddress = view.findViewById(R.id.edtOrderAddress);
        EditText edtPhone = view.findViewById(R.id.edtOrderPhone);
        Spinner spnStatus = view.findViewById(R.id.spnOrderStatus);
        Button btnSave = view.findViewById(R.id.btnSaveUpdate);
        Button btnCancelDialog = view.findViewById(R.id.btnCancelUpdate);

        // Thiết lập Spinner
        String[] statusArray = {"Đang Chờ Duyệt", "Đơn Hàng Bị Hủy", "Đang Chuẩn Bị Hàng", "Đã Giao Cho DVVC", "Hoàn thành"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapter);

        // Đổ dữ liệu cũ
        edtCustomer.setText(currentDoc.getString("customerName"));
        String addr = currentDoc.getString("shippingAddress");
        edtAddress.setText(addr != null ? addr : currentDoc.getString("address"));
        edtPhone.setText(currentDoc.getString("customerPhone"));

        String status = currentDoc.getString("status");
        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equals(status)) {
                spnStatus.setSelection(i);
                break;
            }
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("customerName", edtCustomer.getText().toString().trim());
            updateData.put("shippingAddress", edtAddress.getText().toString().trim());
            updateData.put("customerPhone", edtPhone.getText().toString().trim());
            updateData.put("status", spnStatus.getSelectedItem().toString());

            db.collection("Orders").document(userEmail).collection("user_orders").document(orderId).update(updateData);
            db.collection("Orders").document(orderId).update(updateData).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadOrderDetail(); // Load lại trang chi tiết
            });
        });

        btnCancelDialog.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void updateStatus(String newStatus) {
        db.collection("Orders").document(userEmail).collection("user_orders").document(orderId).update("status", newStatus);
        db.collection("Orders").document(orderId).update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đã cập nhật: " + newStatus, Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}