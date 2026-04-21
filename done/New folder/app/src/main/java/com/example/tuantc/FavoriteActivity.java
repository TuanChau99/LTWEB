package com.example.tuantc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    public static List<Product> favList = new ArrayList<>();

    GridView gvFavorites;
    ProductAdapter adapter;
    TextView tvEmpty;
    ImageView btnBack;

    // --- 1. THÊM DATABASE HELPER ---
    DatabaseHelper dbHelper;
    boolean isAdmin = false;
    String currentUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // --- 2. KHỞI TẠO SQLITE ---
        dbHelper = new DatabaseHelper(this);

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUser = pref.getString("current_user", "Người dùng");

        if (currentUser.equalsIgnoreCase("Admin")) {
            isAdmin = true;
        }

        // --- 3. ĐỌC DỮ LIỆU TỪ SQLITE KHI MỞ TRANG ---
        favList = new ArrayList<>(dbHelper.getFavoriteList(currentUser));

        gvFavorites = findViewById(R.id.gvFavorites);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        setupGridView();
        checkEmpty();

        gvFavorites.setOnItemClickListener((parent, view, position, id) -> {
            Product selected = favList.get(position);
            goToDetail(selected);
        });
    }

    // --- 4. CẬP NHẬT LẠI DATABASE KHI QUAY LẠI TỪ CHI TIẾT ---
    @Override
    protected void onResume() {
        super.onResume();
        // Đọc lại từ DB để đảm bảo nếu ở trang Detail nhấn "Bỏ yêu thích" thì ở đây cập nhật luôn
        if (dbHelper != null) {
            favList.clear();
            favList.addAll(dbHelper.getFavoriteList(currentUser));
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
            checkEmpty();
        }
    }

    private void goToDetail(Product product) {
        Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
        intent.putExtra("pId", product.getId());
        intent.putExtra("pName", product.getName());
        intent.putExtra("pPrice", product.getPrice());
        intent.putExtra("pRating", product.getRating());
        intent.putExtra("isAdmin", isAdmin);

        if (product.getImages() != null) {
            intent.putStringArrayListExtra("pImages", new ArrayList<>(product.getImages()));
        } else {
            intent.putStringArrayListExtra("pImages", new ArrayList<>());
        }
        startActivity(intent);
    }

    private void setupGridView() {
        adapter = new ProductAdapter(this, favList, false, isAdmin);
        gvFavorites.setAdapter(adapter);
    }

    private void checkEmpty() {
        if (favList == null || favList.isEmpty()) {
            if (tvEmpty != null) tvEmpty.setVisibility(View.VISIBLE);
            gvFavorites.setVisibility(View.GONE);
        } else {
            if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
            gvFavorites.setVisibility(View.VISIBLE);
        }
    }
}