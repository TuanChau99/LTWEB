package com.example.tuantc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCategoryActivity extends AppCompatActivity {
    private RecyclerView rvAdminCategories;
    private FloatingActionButton fabAddCat;
    private FirebaseFirestore db;
    private List<Category> categoryList;
    private AdminCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        rvAdminCategories = findViewById(R.id.rvAdminCategories);
        fabAddCat = findViewById(R.id.fabAddCat);

        categoryList = new ArrayList<>();
        setupRecyclerView();
        loadCategories();

        // Sự kiện thêm nhóm mới
        fabAddCat.setOnClickListener(v -> showCategoryDialog(null));
    }

    private void setupRecyclerView() {
        adapter = new AdminCategoryAdapter(this, categoryList, new AdminCategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onEditClick(Category category) {
                showCategoryDialog(category);
            }

            @Override
            public void onDeleteClick(Category category) {
                confirmDelete(category);
            }
        });
        rvAdminCategories.setLayoutManager(new LinearLayoutManager(this));
        rvAdminCategories.setAdapter(adapter);
    }

    private void loadCategories() {
        // Lắng nghe dữ liệu thời gian thực từ Firestore
        db.collection("Categories").addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (value != null) {
                categoryList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Category cat = doc.toObject(Category.class);
                    if (cat != null) {
                        cat.setId(doc.getId());
                        categoryList.add(cat);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showCategoryDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);

        EditText edtName = view.findViewById(R.id.edtCatName);
        EditText edtImage = view.findViewById(R.id.edtCatImage);
        Button btnSave = view.findViewById(R.id.btnSaveCat);

        if (category != null) {
            builder.setTitle("Sửa Nhóm Sản Phẩm");
            edtName.setText(category.getName());
            edtImage.setText(category.getImage());
        } else {
            builder.setTitle("Thêm Nhóm Mới");
        }

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String img = edtImage.getText().toString().trim();

            if (name.isEmpty()) {
                edtName.setError("Không được để trống");
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("image", img);

            if (category == null) {
                // Thêm mới
                db.collection("Categories").add(data)
                        .addOnSuccessListener(documentReference -> Toast.makeText(this, "Đã thêm!", Toast.LENGTH_SHORT).show());
            } else {
                // Cập nhật
                db.collection("Categories").document(category.getId()).update(data)
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đã cập nhật!", Toast.LENGTH_SHORT).show());
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void confirmDelete(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa nhóm '" + category.getName() + "'?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    db.collection("Categories").document(category.getId()).delete()
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}