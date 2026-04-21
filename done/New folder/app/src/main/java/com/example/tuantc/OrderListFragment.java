package com.example.tuantc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListFragment extends Fragment {
    private String filterStatus;
    private boolean isAdmin;

    private List<OrderModel> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    private ListView lvOrders;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private FirebaseFirestore db;

    public OrderListFragment() {}

    public OrderListFragment(String status, boolean isAdmin) {
        this.filterStatus = status;
        this.isAdmin = isAdmin;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        db = FirebaseFirestore.getInstance();
        lvOrders = view.findViewById(R.id.lvOrderFragment);
        progressBar = view.findViewById(R.id.progressBarOrder);
        layoutEmpty = view.findViewById(R.id.tvEmptyOrder);

        orderAdapter = new OrderAdapter(getContext(), orderList, isAdmin);
        lvOrders.setAdapter(orderAdapter);

        lvOrders.setOnItemClickListener((parent, v, position, id) -> {
            OrderModel selectedOrder = orderList.get(position);
            if (isAdmin) {
                showUpdateOrderDialog(selectedOrder);
            } else {
                Toast.makeText(getContext(), "Đơn hàng: " + selectedOrder.getStatus(), Toast.LENGTH_SHORT).show();
            }
        });

        loadOrdersFromFirestore();
        return view;
    }

    private void loadOrdersFromFirestore() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        // Lấy từ collection gốc "Orders" để Admin quản lý tập trung
        Query query = db.collection("Orders").orderBy("dateOrder", Query.Direction.DESCENDING);

        query.addSnapshotListener((value, error) -> {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            if (error != null) return;

            if (value != null) {
                orderList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    OrderModel order = doc.toObject(OrderModel.class);
                    if (order != null) {
                        order.setOrderId(doc.getId());
                        if (filterStatus == null || filterStatus.equals("Tất cả") || filterStatus.equals(order.getStatus())) {
                            orderList.add(order);
                        }
                    }
                }
                if (layoutEmpty != null) layoutEmpty.setVisibility(orderList.isEmpty() ? View.VISIBLE : View.GONE);
                orderAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showUpdateOrderDialog(OrderModel order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_order, null);

        EditText edtName = dialogView.findViewById(R.id.edtOrderCustomer);
        EditText edtAddress = dialogView.findViewById(R.id.edtOrderAddress);
        EditText edtPhone = dialogView.findViewById(R.id.edtOrderPhone);
        Spinner spnStatus = dialogView.findViewById(R.id.spnOrderStatus);

        edtName.setText(order.getCustomerName());
        edtAddress.setText(order.getAddress());
        edtPhone.setText(order.getCustomerPhone());

        String[] statuses = {"Đang Chờ Duyệt", "Đang Chuẩn Bị Hàng", "Đang giao", "Hoàn thành", "Đã hủy"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(statusAdapter);

        for (int i = 0; i < statuses.length; i++) {
            if (order.getStatus().equalsIgnoreCase(statuses[i])) {
                spnStatus.setSelection(i);
                break;
            }
        }

        builder.setView(dialogView);
        builder.setPositiveButton("LƯU", (dialog, which) -> {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("customerName", edtName.getText().toString());
            updateData.put("address", edtAddress.getText().toString());
            updateData.put("customerPhone", edtPhone.getText().toString());
            updateData.put("status", spnStatus.getSelectedItem().toString());

            db.collection("Orders").document(order.getOrderId())
                    .update(updateData)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
        builder.setNegativeButton("HỦY", null);
        builder.show();
    }
}