package com.example.tuantc;

import android.content.Intent;
import android.content.SharedPreferences; // 1. Thêm import này
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    public static ArrayList<Product> favList = new ArrayList<>();

    GridView gvFavorites;
    ProductAdapter adapter;
    TextView tvEmpty;
    ImageView btnBack;

    // 2. Khai báo biến isAdmin
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // 3. Lấy quyền Admin từ SharedPreferences
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "Người dùng");
        if (currentUser.equalsIgnoreCase("Admin")) {
            isAdmin = true;
        }

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

    private void goToDetail(Product product) {
        Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
        intent.putExtra("pId", product.getId());
        intent.putExtra("pName", product.getName());
        intent.putExtra("pPrice", product.getPrice());
        intent.putExtra("pRating", product.getRating());
        // Truyền kèm isAdmin sang trang Detail để đồng bộ
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
        if (favList.isEmpty()) {
            if (tvEmpty != null) tvEmpty.setVisibility(View.VISIBLE);
            gvFavorites.setVisibility(View.GONE);
        } else {
            if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
            gvFavorites.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            checkEmpty();
        }
    }
}