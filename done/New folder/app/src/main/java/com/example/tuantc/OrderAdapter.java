package com.example.tuantc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderModel> list;
    private boolean isAdmin;
    public OrderAdapter(Context context, List<OrderModel> list, boolean isAdmin) {
        this.context = context;
        this.list = list;
        this.isAdmin = isAdmin;
    }

    @Override
    public int getCount() { return list.size(); }
    @Override
    public Object getItem(int i) { return list.get(i); }
    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        }
        OrderModel order = list.get(position);

        TextView tvId = convertView.findViewById(R.id.tvOrderId);
        TextView tvStatus = convertView.findViewById(R.id.tvOrderStatus);
        TextView tvTotal = convertView.findViewById(R.id.tvOrderTotal);

        tvId.setText("Mã đơn: " + order.getOrderId());
        tvStatus.setText(order.getStatus());
        tvTotal.setText("Tổng thanh toán: " + order.getTotalPrice());

        // Click vào đơn hàng để xem chi tiết hoặc xử lý Admin
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("isAdmin", isAdmin);
            context.startActivity(intent);
        });

        return convertView;
    }
}