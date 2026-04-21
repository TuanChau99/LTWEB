package com.example.tuantc;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminOrderActivity extends AppCompatActivity {
    ListView lvOrders;
    ImageView btnBack;
    FirebaseFirestore db;
    List<OrderModel> orderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        db = FirebaseFirestore.getInstance();
        lvOrders = findViewById(R.id.lvAdminOrders);
        btnBack = findViewById(R.id.btnBackAdminOrder);

        btnBack.setOnClickListener(v -> finish());

        loadAllOrders();

        // Sự kiện khi nhấn vào 1 đơn hàng để cập nhật
        lvOrders.setOnItemClickListener((parent, view, position, id) -> {
            OrderModel selectedOrder = orderList.get(position);
            showUpdateOrderDialog(selectedOrder);
        });
    }

    private void loadAllOrders() {
        orderList = new ArrayList<>();
        db.collection("Orders")
                .orderBy("dateOrder", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        OrderModel order = doc.toObject(OrderModel.class);
                        if (order != null) {
                            order.setOrderId(doc.getId());
                            orderList.add(order);
                        }
                    }
                });
    }

    private void showUpdateOrderDialog(OrderModel order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_update_order, null);
        builder.setView(view);

        EditText edtCustomer = view.findViewById(R.id.edtOrderCustomer);
        EditText edtAddress = view.findViewById(R.id.edtOrderAddress);
        EditText edtPhone = view.findViewById(R.id.edtOrderPhone);
        Spinner spnStatus = view.findViewById(R.id.spnOrderStatus);
        Button btnSave = view.findViewById(R.id.btnSaveUpdate);
        Button btnCancel = view.findViewById(R.id.btnCancelUpdate);

        String[] statusArray = {"Đang Chờ Duyệt", "Đơn Hàng Bị Hủy", "Đang Chuẩn Bị Hàng", "Đã Giao Cho DVVC", "Hoàn thành"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapter);

        edtCustomer.setText(order.getCustomerName());
        edtAddress.setText(order.getShippingAddress() != null ? order.getShippingAddress() : order.getAddress());
        edtPhone.setText(order.getCustomerPhone());

        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equals(order.getStatus())) {
                spnStatus.setSelection(i);
                break;
            }
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("customerName", edtCustomer.getText().toString());
            updateData.put("shippingAddress", edtAddress.getText().toString());
            updateData.put("customerPhone", edtPhone.getText().toString());
            updateData.put("status", spnStatus.getSelectedItem().toString());

            db.collection("Orders").document(order.getOrderId()).update(updateData);
            db.collection("Orders").document(order.getCustomerEmail())
                    .collection("user_orders").document(order.getOrderId())
                    .update(updateData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadAllOrders(); // Tải lại danh sách
                    });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}