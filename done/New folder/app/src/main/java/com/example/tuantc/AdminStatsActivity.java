package com.example.tuantc;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminStatsActivity extends AppCompatActivity {
    private TextView tvTotalRevenue, tvTotalOrders, tvTotalProducts;
    private BarChart barChartRevenue;
    private PieChart pieChartStatus;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats);

        db = FirebaseFirestore.getInstance();
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        barChartRevenue = findViewById(R.id.barChartRevenue);
        pieChartStatus = findViewById(R.id.pieChartStatus);

        loadStatistics();
    }

    private void loadStatistics() {
        // 1. Thống kê sản phẩm
        db.collection("Product").get().addOnSuccessListener(snapshots -> {
            tvTotalProducts.setText(String.valueOf(snapshots.size()));
        });

        // 2. Thống kê đơn hàng, doanh thu và trạng thái
        db.collection("Orders").get().addOnSuccessListener(snapshots -> {
            long totalRevenue = 0;
            int orderCount = snapshots.size();

            // Map để đếm trạng thái đơn hàng (đã giao, hủy, ...)
            Map<String, Integer> statusMap = new HashMap<>();
            // List cho biểu đồ cột doanh thu
            List<BarEntry> barEntries = new ArrayList<>();
            float index = 1;

            for (DocumentSnapshot doc : snapshots) {
                // Xử lý doanh thu
                Object priceObj = doc.get("totalPrice");
                if (priceObj != null) {
                    try {
                        String priceStr = priceObj.toString().replaceAll("[^0-9]", "");
                        long price = Long.parseLong(priceStr);
                        totalRevenue += price;
                        barEntries.add(new BarEntry(index++, price));
                    } catch (Exception e) { e.printStackTrace(); }
                }

                // Xử lý trạng thái
                String status = doc.getString("status");
                if (status == null || status.isEmpty()) status = "Chờ xử lý";
                statusMap.put(status, statusMap.getOrDefault(status, 0) + 1);
            }

            tvTotalRevenue.setText(String.format("%,d đ", totalRevenue));
            tvTotalOrders.setText(String.valueOf(orderCount));

            setupBarChart(barEntries);
            setupPieChart(statusMap);
        });
    }

    private void setupBarChart(List<BarEntry> entries) {
        if (entries.isEmpty()) return;

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu từng đơn");
        dataSet.setColor(Color.parseColor("#EF4444"));
        dataSet.setValueTextColor(Color.parseColor("#1E293B"));
        dataSet.setValueTextSize(10f);

        BarData data = new BarData(dataSet);
        barChartRevenue.setData(data);
        barChartRevenue.getDescription().setEnabled(false);
        barChartRevenue.animateY(1000);
        barChartRevenue.invalidate();
    }

    private void setupPieChart(Map<String, Integer> statusMap) {
        List<PieEntry> pieEntries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : statusMap.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        // Sử dụng bảng màu Material
        int[] colors = {
                Color.parseColor("#10B981"), // Xanh lá
                Color.parseColor("#3B82F6"), // Xanh dương
                Color.parseColor("#F59E0B"), // Vàng
                Color.parseColor("#EF4444"), // Đỏ
                Color.parseColor("#8B5CF6")  // Tím
        };
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChartStatus.setData(data);
        pieChartStatus.setUsePercentValues(true);
        pieChartStatus.setEntryLabelColor(Color.BLACK);
        pieChartStatus.setCenterText("Tỉ lệ đơn hàng");
        pieChartStatus.setCenterTextSize(16f);
        pieChartStatus.getDescription().setEnabled(false);
        pieChartStatus.animateXY(1000, 1000);
        pieChartStatus.invalidate();
    }
}