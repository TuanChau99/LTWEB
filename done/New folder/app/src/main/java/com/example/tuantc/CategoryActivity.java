package com.example.tuantc;

import android.content.Intent;
import android.content.SharedPreferences; // Thêm import này
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    TextView tvCategoryTitle;
    GridView gvProducts;
    LinearLayout layoutQuickCategories;
    FirebaseFirestore firestore;
    List<Product> productList = new ArrayList<>();
    ProductAdapter adapter;

    // --- BƯỚC 1: KHAI BÁO BIẾN isAdmin ---
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        firestore = FirebaseFirestore.getInstance();
        String currentCat = getIntent().getStringExtra("CATEGORY_NAME");

        // --- BƯỚC 2: LẤY QUYỀN ADMIN TỪ SHARED PREFERENCES ---
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "Người dùng");
        if (currentUser.equalsIgnoreCase("Admin")) {
            isAdmin = true;
        }

        // Ánh xạ
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        gvProducts = findViewById(R.id.gvCategoryProducts);
        layoutQuickCategories = findViewById(R.id.layoutQuickCategories);
        ImageView btnBack = findViewById(R.id.btnBackCat);

        btnBack.setOnClickListener(v -> finish());

        tvCategoryTitle.setText("Danh mục: " + currentCat);

        loadQuickSelectionBar();
        loadProductsByCategory(currentCat);

        gvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product selected = productList.get(position);
            Intent intent = new Intent(CategoryActivity.this, DetailActivity.class);

            intent.putExtra("pId", selected.getId());
            intent.putExtra("pName", selected.getName());
            intent.putExtra("pPrice", selected.getPrice());
            intent.putExtra("pRating", selected.getRating());
            // Truyền thêm quyền isAdmin sang Detail nếu cần thiết
            intent.putExtra("isAdmin", isAdmin);

            if (selected.getImages() != null) {
                intent.putStringArrayListExtra("pImages", new ArrayList<>(selected.getImages()));
            } else {
                intent.putStringArrayListExtra("pImages", new ArrayList<>());
            }

            startActivity(intent);
        });
    }

    private void loadQuickSelectionBar() {
        firestore.collection("Categories").get().addOnSuccessListener(snapshots -> {
            layoutQuickCategories.removeAllViews();
            for (DocumentSnapshot doc : snapshots) {
                String name = doc.getString("name");
                String imgName = doc.getString("image");

                LinearLayout item = new LinearLayout(this);
                item.setOrientation(LinearLayout.VERTICAL);
                item.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.WRAP_CONTENT);
                p.setMargins(0, 0, 30, 0);
                item.setLayoutParams(p);

                ShapeableImageView img = new ShapeableImageView(this);
                int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
                img.setImageResource(resId != 0 ? resId : android.R.drawable.ic_menu_gallery);
                img.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
                img.setShapeAppearanceModel(ShapeAppearanceModel.builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build());
                img.setStrokeWidth(2f);
                img.setStrokeColor(getColorStateList(android.R.color.darker_gray));

                TextView txt = new TextView(this);
                txt.setText(name);
                txt.setTextSize(10f);
                txt.setGravity(Gravity.CENTER);

                item.addView(img);
                item.addView(txt);

                item.setOnClickListener(v -> {
                    tvCategoryTitle.setText("Danh mục: " + name);
                    loadProductsByCategory(name);
                });

                layoutQuickCategories.addView(item);
            }
        });
    }

    private void loadProductsByCategory(String categoryName) {
        firestore.collection("Product")
                .whereEqualTo("category", categoryName)
                .get()
                .addOnSuccessListener(snapshots -> {
                    productList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Product p = doc.toObject(Product.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            productList.add(p);
                        }
                    }
                    // --- BƯỚC 3: TRUYỀNisAdmin VÀO ADAPTER ---
                    // Tham số: (Context, List, isCart, isAdmin)
                    adapter = new ProductAdapter(this, productList, false, isAdmin);
                    gvProducts.setAdapter(adapter);
                });
    }
}