package com.example.tuantc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {
    private String filterStatus;
    private boolean isAdmin;

    private List<OrderModel> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    private ListView lvOrders;
    private ProgressBar progressBar;
    private View layoutEmpty;

    public OrderListFragment() {
    }

    public OrderListFragment(String status, boolean isAdmin) {
        this.filterStatus = status;
        this.isAdmin = isAdmin;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        lvOrders = view.findViewById(R.id.lvOrderFragment);
        progressBar = view.findViewById(R.id.progressBarOrder);
        layoutEmpty = view.findViewById(R.id.tvEmptyOrder);

        //  Truyền thêm biến isAdmin vào OrderAdapter ---
        orderAdapter = new OrderAdapter(getContext(), orderList, isAdmin);
        lvOrders.setAdapter(orderAdapter);
        // ------------------------------------------------------------

        loadOrdersFromFirestore();

        return view;
    }

    private void loadOrdersFromFirestore() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = "tuantc@vimenstore.com";

        db.collection("Orders").document(userEmail)
                .collection("user_orders")
                .addSnapshotListener((value, error) -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    if (error != null) return;

                    if (value != null) {
                        orderList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            OrderModel order = doc.toObject(OrderModel.class);
                            if (order != null) {
                                if (filterStatus == null || filterStatus.equals("Tất cả") || filterStatus.equals(order.getStatus())) {
                                    orderList.add(order);
                                }
                            }
                        }

                        if (orderList.isEmpty()) {
                            if (layoutEmpty != null) layoutEmpty.setVisibility(View.VISIBLE);
                        } else {
                            if (layoutEmpty != null) layoutEmpty.setVisibility(View.GONE);
                        }

                        orderAdapter.notifyDataSetChanged();
                    }
                });
    }
}