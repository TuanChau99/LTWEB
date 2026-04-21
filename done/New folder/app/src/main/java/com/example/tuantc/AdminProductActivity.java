package com.example.tuantc;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class AdminProductActivity extends AppCompatActivity {
    private RecyclerView rvAdminProducts;
    private FloatingActionButton fabAddProduct;
    private FirebaseFirestore db;
    private List<Product> productList;
    private AdminProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        db = FirebaseFirestore.getInstance();
        rvAdminProducts = findViewById(R.id.rvAdminProducts);
        fabAddProduct = findViewById(R.id.fabAddProductMain);

        productList = new ArrayList<>();
        adapter = new AdminProductAdapter(this, productList, new AdminProductAdapter.OnProductClickListener() {
            @Override
            public void onEditClick(Product product) {
                // Gọi lại hàm showEditProductDialog
            }

            @Override
            public void onDeleteClick(Product product) {
                confirmDelete(product);
            }
        });

        rvAdminProducts.setLayoutManager(new LinearLayoutManager(this));
        rvAdminProducts.setAdapter(adapter);

        loadProducts();
    }

    private void loadProducts() {
        db.collection("Product").addSnapshotListener((value, error) -> {
            if (value != null) {
                productList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Product p = doc.toObject(Product.class);
                    if (p != null) {
                        p.setId(doc.getId());
                        productList.add(p);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void confirmDelete(Product p) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Xóa sản phẩm " + p.getName() + "?")
                .setPositiveButton("Xóa", (d, w) -> {
                    db.collection("Product").document(p.getId()).delete();
                })
                .setNegativeButton("Hủy", null).show();
    }
}