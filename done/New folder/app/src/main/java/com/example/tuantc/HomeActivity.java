package com.example.tuantc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.graphics.Color;
import android.content.SharedPreferences;

public class HomeActivity extends AppCompatActivity {
    ImageView btnSearchHeader, btnNotification;
    GridView gvProducts;
    TextView tvSeeAll, tvSearchTitle, tvAddCategory;
    LinearLayout layoutHomeContent, layoutCategories;
    List<Product> productList, searchList;
    ProductAdapter adapter;

    RecyclerView rvNewProducts, rvBestSellers;
    List<Product> newProductsList, bestSellersList;
    ProductHorizontalAdapter newAdapter, bestAdapter;

    LinearLayout navHome, navFavorite, navCart, navProfile;
    FloatingActionButton fabAddProduct;
    private FirebaseFirestore firestore;
    ViewPager2 viewPagerBanner;
    private Handler bannerHandler = new Handler(Looper.getMainLooper());
    private boolean isAdmin = false;

    // Danh sách tên category để dùng cho Spinner khi thêm sản phẩm
    List<String> categoryNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        // Kiểm tra thêm từ SharedPreferences
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (pref.getString("current_user", "").equalsIgnoreCase("Admin")) {
            isAdmin = true;
        }
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        newProductsList = new ArrayList<>();
        bestSellersList = new ArrayList<>();

        initViews();
        setupRecyclerViews();
        setupNavigation();

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        Log.d("DEBUG_ADMIN", "Giá trị isAdmin nhận được là: " + isAdmin);
        firestore = FirebaseFirestore.getInstance();

        if (!isAdmin) {
            if (fabAddProduct != null) fabAddProduct.setVisibility(View.GONE);
            if (tvAddCategory != null) tvAddCategory.setVisibility(View.GONE);
        } else {
            if (tvAddCategory != null) tvAddCategory.setVisibility(View.VISIBLE);
        }

        loadCategoriesFromFirestore();
        setupBanner();
        loadProductsFromFirestore();

        btnSearchHeader.setOnClickListener(v -> showSearchDialog());

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(this, "Bạn không có thông báo mới", Toast.LENGTH_SHORT).show()
            );
        }

        if (tvSeeAll != null) {
            tvSeeAll.setText("See all »");
            tvSeeAll.setVisibility(View.VISIBLE);
            tvSeeAll.setOnClickListener(v -> {
                searchList.clear();
                searchList.addAll(productList);
                adapter.notifyDataSetChanged();
                updateGridViewHeight();
                tvSeeAll.setVisibility(View.GONE);
            });
        }

        if (fabAddProduct != null) fabAddProduct.setOnClickListener(v -> showAddProductDialog());
        if (tvAddCategory != null) tvAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        gvProducts.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy đúng sản phẩm từ searchList dựa trên vị trí click
            Product selected = searchList.get(position);

            Log.d("DEBUG_CLICK", "Đã click vào: " + selected.getName() + " | isAdmin: " + isAdmin);

            if (isAdmin) {
                // Tạo bảng chọn cho Admin
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Quản lý: " + selected.getName());

                String[] options = {"Chỉnh sửa", "Xóa sản phẩm", "Xem chi tiết (Khách)"};

                builder.setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            showEditProductDialog(selected, position);
                            break;
                        case 1:
                            showDeleteConfirmDialog(selected, position);
                            break;
                        case 2:
                            goToDetail(selected);
                            break;
                    }
                });
                builder.show();
            } else {
                // Nếu không phải admin thì đi thẳng tới chi tiết
                goToDetail(selected);
            }
        });
        // SỰ KIỆN NHẤN GIỮ ĐỂ XÓA SẢN PHẨM (DÀNH CHO ADMIN)
        gvProducts.setOnItemLongClickListener((parent, view, position, id) -> {
            if (isAdmin) {
                Product selected = searchList.get(position);

                // Hiện bảng xác nhận xóa
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Quản lý sản phẩm")
                        .setMessage("Bạn có muốn xóa sản phẩm '" + selected.getName() + "' không?")
                        .setPositiveButton("Xóa ngay", (dialog, which) -> {
                            showDeleteConfirmDialog(selected, position);
                        })
                        .setNeutralButton("Chỉnh sửa", (dialog, which) -> {
                            showEditProductDialog(selected, position);
                        })
                        .setNegativeButton("Hủy", null)
                        .show();

                return true; // Trả về true để máy hiểu là đã xử lý xong, không chạy thêm sự kiện click thường
            }
            return false; // Nếu không phải admin thì không làm gì cả
        });
    }

    private void initViews() {
        btnSearchHeader = findViewById(R.id.btnSearchHeader);
        btnNotification = findViewById(R.id.btnNotification);
        gvProducts = findViewById(R.id.gvProducts);
        tvSeeAll = findViewById(R.id.tvSeeAll);
        tvAddCategory = findViewById(R.id.tvAddCategory);
        viewPagerBanner = findViewById(R.id.viewPagerBanner);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        rvNewProducts = findViewById(R.id.rvNewProducts);
        rvBestSellers = findViewById(R.id.rvBestSellers);
        navHome = findViewById(R.id.navHome);
        navFavorite = findViewById(R.id.navFavorite);
        navCart = findViewById(R.id.navCart);
        navProfile = findViewById(R.id.navProfile);
        tvSearchTitle = findViewById(R.id.tvSearchTitle);
        layoutHomeContent = findViewById(R.id.layoutHomeContent);
        layoutCategories = findViewById(R.id.layoutCategories);
    }

    private void loadCategoriesFromFirestore() {
        firestore.collection("Categories").get().addOnSuccessListener(snapshots -> {
            layoutCategories.removeAllViews();
            categoryNames.clear();
            if (snapshots.isEmpty()) {
                createDefaultCategoriesOnFirestore();
            } else {
                for (DocumentSnapshot doc : snapshots) {
                    String name = doc.getString("name");
                    String img = doc.getString("image");
                    String id = doc.getId();
                    categoryNames.add(name);
                    addNewCategoryToUI(id, name, img);
                }
            }
        });
    }

    private void createDefaultCategoriesOnFirestore() {
        String[][] defaults = {{"Áo", "so_mi"}, {"Quần", "quan_tay"}, {"Giày", "giay_da"}, {"Vest", "ao_vest"}};
        for (String[] cat : defaults) {
            Map<String, Object> data = new HashMap<>();
            data.put("name", cat[0]);
            data.put("image", cat[1]);
            firestore.collection("Categories").add(data).addOnSuccessListener(doc -> {
                categoryNames.add(cat[0]);
                addNewCategoryToUI(doc.getId(), cat[0], cat[1]);
            });
        }
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Danh Mục Mới");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        final EditText inName = new EditText(this); inName.setHint("Tên danh mục (vd: Vest)"); layout.addView(inName);
        final EditText inImg = new EditText(this); inImg.setHint("Tên ảnh drawable (vd: ao_vest)"); layout.addView(inImg);
        builder.setView(layout);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = inName.getText().toString().trim();
            String img = inImg.getText().toString().trim();
            if (!name.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("name", name); data.put("image", img);
                firestore.collection("Categories").add(data).addOnSuccessListener(doc -> {
                    categoryNames.add(name);
                    addNewCategoryToUI(doc.getId(), name, img);
                    Toast.makeText(this, "Đã thêm danh mục", Toast.LENGTH_SHORT).show();
                });
            }
        });
        builder.setNegativeButton("Hủy", null).show();
    }

    private void addNewCategoryToUI(String id, String name, String imgName) {
        // 1. Tạo container cho từng item danh mục
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 40, 0); // Khoảng cách giữa các danh mục
        item.setLayoutParams(params);

        // 2. Tạo icon danh mục
        ShapeableImageView img = new ShapeableImageView(this);
        int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
        img.setImageResource(resId != 0 ? resId : android.R.drawable.ic_menu_gallery);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        img.setStrokeColor(getColorStateList(android.R.color.darker_gray));
        img.setStrokeWidth(2f);
        img.setShapeAppearanceModel(ShapeAppearanceModel.builder()
                .setAllCornerSizes(ShapeAppearanceModel.PILL).build());

        // 3. Tạo chữ tên danh mục
        TextView txt = new TextView(this);
        txt.setText(name);
        txt.setTextSize(12f);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(0, 10, 0, 0);

        // Thêm hình và chữ vào item
        item.addView(img);
        item.addView(txt);

        // --- CLICK ĐỂ MỞ TRANG DANH MỤC RIÊNG (ẨN BANNER) ---
        item.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("CATEGORY_NAME", name); // Gửi tên danh mục sang trang mới
            startActivity(intent);
        });

        // 4. CHỨC NĂNG XÓA CHO ADMIN (Nhấn giữ lâu)
        item.setOnLongClickListener(v -> {
            if (isAdmin) {
                new AlertDialog.Builder(this)
                        .setTitle("Xóa danh mục")
                        .setMessage("Bạn có muốn xóa danh mục '" + name + "' không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            firestore.collection("Categories").document(id).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        layoutCategories.removeView(item);
                                        categoryNames.remove(name);
                                        Toast.makeText(this, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("Hủy", null).show();
                return true;
            }
            return false;
        });

        // Thêm toàn bộ item vào thanh trượt danh mục trên Home
        layoutCategories.addView(item);
    }

    private void loadProductsFromFirestore() {
        firestore.collection("Product").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                if (!snapshots.isEmpty()) {
                    productList.clear();
                    newProductsList.clear();
                    bestSellersList.clear();
                    searchList.clear();

                    int i = 0;
                    for (DocumentSnapshot doc : snapshots) {
                        Product p = doc.toObject(Product.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            productList.add(p);
                            if (i < 5) newProductsList.add(p);
                            else if (i < 10) bestSellersList.add(p);
                            if (i < 6) searchList.add(p);
                            i++;
                        }
                    }

                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        newAdapter.notifyDataSetChanged();
                        bestAdapter.notifyDataSetChanged();
                        updateGridViewHeight();
                    });
                }
            }
        });
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tìm kiếm sản phẩm");
        final EditText input = new EditText(this); input.setHint("Nhập tên sản phẩm...");
        LinearLayout container = new LinearLayout(this); container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(40, 20, 40, 20); container.addView(input);
        builder.setView(container);
        builder.setPositiveButton("Tìm kiếm", (dialog, which) -> filterProducts(input.getText().toString().trim()));
        builder.setNegativeButton("Hủy", (dialog, which) -> filterProducts(""));
        builder.show();
    }

    private void filterProducts(String text) {
        searchList.clear();
        if (text.isEmpty()) {
            for (int i = 0; i < Math.min(6, productList.size()); i++) searchList.add(productList.get(i));
            if (layoutHomeContent != null) layoutHomeContent.setVisibility(View.VISIBLE);
            if (tvSearchTitle != null) tvSearchTitle.setVisibility(View.GONE);
            if (tvSeeAll != null) tvSeeAll.setVisibility(View.VISIBLE);
        } else {
            for (Product p : productList) {
                if (p.getName().toLowerCase().contains(text.toLowerCase())) searchList.add(p);
            }
            if (layoutHomeContent != null) layoutHomeContent.setVisibility(View.GONE);
            if (tvSearchTitle != null) {
                tvSearchTitle.setVisibility(View.VISIBLE);
                tvSearchTitle.setText("Kết quả cho: \"" + text + "\"");
            }
            if (tvSeeAll != null) tvSeeAll.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
        updateGridViewHeight();
    }

    private void filterByCategory(String categoryName) {
        searchList.clear();
        for (Product p : productList) {
            if (p.getCategory() != null && p.getCategory().equalsIgnoreCase(categoryName)) searchList.add(p);
        }
        if (layoutHomeContent != null) layoutHomeContent.setVisibility(View.GONE);
        if (tvSearchTitle != null) {
            tvSearchTitle.setVisibility(View.VISIBLE);
            tvSearchTitle.setText("Danh mục: " + categoryName);
        }
        if (tvSeeAll != null) tvSeeAll.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
        updateGridViewHeight();
    }

    private void setupNavigation() {
        navHome.setOnClickListener(v -> filterProducts(""));
        navFavorite.setOnClickListener(v -> startActivity(new Intent(this, FavoriteActivity.class)));
        navCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void setupRecyclerViews() {
        rvNewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvBestSellers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newAdapter = new ProductHorizontalAdapter(this, newProductsList);
        bestAdapter = new ProductHorizontalAdapter(this, bestSellersList);
        rvNewProducts.setAdapter(newAdapter);
        rvBestSellers.setAdapter(bestAdapter);
        adapter = new ProductAdapter(this, searchList, false, isAdmin);
        gvProducts.setAdapter(adapter);
        gvProducts.setFocusable(false);
        gvProducts.setNestedScrollingEnabled(false);
    }

    private void goToDetail(Product product) {
        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        intent.putExtra("pId", product.getId());
        intent.putExtra("pName", product.getName());
        intent.putExtra("pPrice", product.getPrice());
        intent.putExtra("pOldPrice", product.getOldPrice());
        intent.putExtra("pRating", product.getRating());

        // QUAN TRỌNG: Phải gửi đúng mảng ảnh từ Firebase
        if (product.getImages() != null) {
            intent.putStringArrayListExtra("pImages", new ArrayList<>(product.getImages()));
        } else {
            // Nếu không có ảnh, gửi mảng rỗng để tránh bị Crash
            intent.putStringArrayListExtra("pImages", new ArrayList<>());
        }
        startActivity(intent);
    }

    private void updateGridViewHeight() {
        gvProducts.post(() -> setGridViewHeightBasedOnChildren(gvProducts, 2));
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        android.widget.ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null || listAdapter.getCount() == 0) {
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = 400;
            gridView.setLayoutParams(params);
            return;
        }
        int items = listAdapter.getCount();
        int rows = (int) Math.ceil((double) items / columns);
        int totalHeight = 0;
        try {
            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int singleRowHeight = listItem.getMeasuredHeight();
            if (singleRowHeight <= 0) singleRowHeight = 650;
            totalHeight = (singleRowHeight + 40) * rows;
        } catch (Exception e) {
            totalHeight = 1800;
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + 100;
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    private void showEditProductDialog(Product product, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chỉnh Sửa Sản Phẩm");

        // Container chính
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 40, 60, 20);

        // 1. Nhập Tên
        final EditText inName = new EditText(this);
        inName.setHint("Tên sản phẩm");
        inName.setText(product.getName());
        layout.addView(inName);

        // 2. Nhập Giá hiện tại
        final EditText inPrice = new EditText(this);
        inPrice.setHint("Giá hiện tại (ví dụ: 350.000)");
        inPrice.setText(product.getPrice());
        layout.addView(inPrice);

        // 3. Nhập Giá gốc (để gạch ngang)
        final EditText inOldPrice = new EditText(this);
        inOldPrice.setHint("Giá gốc (ví dụ: 550.000)");
        inOldPrice.setText(product.getOldPrice());
        layout.addView(inOldPrice);

        // 4. NHÃN VÀ Ô NHẬP DANH SÁCH ẢNH (TỐI ƯU MỚI)
        TextView tvImgLabel = new TextView(this);
        tvImgLabel.setText("Danh sách tên ảnh (cách nhau bởi dấu phẩy):");
        tvImgLabel.setPadding(10, 20, 0, 5);
        tvImgLabel.setTextSize(14f);
        layout.addView(tvImgLabel);

        final EditText inImages = new EditText(this);
        inImages.setHint("ví dụ: ao_vest, ao_vest_den, ao_vest_xanh");

        // Hiển thị danh sách ảnh hiện có nối lại bằng dấu phẩy
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                inImages.setText(String.join(", ", product.getImages()));
            } else {
                // Cách thủ công cho máy đời cũ hơn
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < product.getImages().size(); i++) {
                    sb.append(product.getImages().get(i));
                    if (i < product.getImages().size() - 1) sb.append(", ");
                }
                inImages.setText(sb.toString());
            }
        }
        layout.addView(inImages);

        // 5. Chọn Danh mục
        TextView tvCat = new TextView(this);
        tvCat.setText("Danh mục:");
        tvCat.setPadding(10, 20, 0, 5);
        layout.addView(tvCat);

        final Spinner spnCat = new Spinner(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCat.setAdapter(dataAdapter);
        if (product.getCategory() != null) spnCat.setSelection(categoryNames.indexOf(product.getCategory()));
        layout.addView(spnCat);

        builder.setView(layout);

        // NÚT CẬP NHẬT
        builder.setPositiveButton("Cập Nhật", (dialog, which) -> {
            String name = inName.getText().toString().trim();
            String price = inPrice.getText().toString().trim();
            String oldP = inOldPrice.getText().toString().trim();
            String cat = spnCat.getSelectedItem().toString();

            // XỬ LÝ CHUỖI ẢNH NHẬP VÀO THÀNH DANH SÁCH (LIST)
            String imgString = inImages.getText().toString().trim();
            List<String> newImagesList = new ArrayList<>();
            if (!imgString.isEmpty()) {
                String[] parts = imgString.split(","); // Tách chuỗi theo dấu phẩy
                for (String s : parts) {
                    String cleanName = s.trim();
                    if (!cleanName.isEmpty()) newImagesList.add(cleanName);
                }
            }

            // Cập nhật lên Firebase Firestore
            firestore.collection("Product").document(product.getId())
                    .update(
                            "name", name,
                            "price", price,
                            "oldPrice", oldP,
                            "images", newImagesList, // Gửi mảng ảnh mới
                            "category", cat
                    )
                    .addOnSuccessListener(aVoid -> {
                        // Cập nhật model local để GridView đổi ngay lập tức
                        product.setName(name);
                        product.setPrice(price);
                        product.setOldPrice(oldP);
                        product.setImages(newImagesList);
                        product.setCategory(cat);

                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Đã cập nhật sản phẩm!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        // NÚT XÓA (Màu đỏ)
        builder.setNegativeButton("XÓA SẢN PHẨM", (dialog, which) -> {
            showDeleteConfirmDialog(product, position);
        });

        // NÚT CHI TIẾT
        builder.setNeutralButton("Chi tiết", (dialog, which) -> goToDetail(product));

        AlertDialog dialog = builder.create();
        dialog.show();

        // Làm nút Xóa nổi bật bằng màu đỏ
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }

    private void showDeleteConfirmDialog(Product product, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn xóa '" + product.getName() + "' không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa trên Firestore
                    firestore.collection("Product").document(product.getId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                // Xóa trong List local để màn hình cập nhật ngay
                                productList.remove(product);
                                searchList.remove(position);
                                adapter.notifyDataSetChanged();
                                updateGridViewHeight();
                                Toast.makeText(this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Sản Phẩm Mới");

        // Container chính cho các ô nhập liệu
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 40, 60, 20);

        final EditText inName = new EditText(this);
        inName.setHint("Tên sản phẩm");
        layout.addView(inName);

        final EditText inPrice = new EditText(this);
        inPrice.setHint("Giá hiện tại (vd: 350.000)");
        layout.addView(inPrice);

        final EditText inOldPrice = new EditText(this);
        inOldPrice.setHint("Giá gốc (để gạch ngang - vd: 550.000)");
        layout.addView(inOldPrice);

        // --- TỐI ƯU PHẦN NHẬP NHIỀU ẢNH ---
        TextView tvImgLabel = new TextView(this);
        tvImgLabel.setText("Danh sách tên ảnh (cách nhau bởi dấu phẩy):");
        tvImgLabel.setPadding(10, 20, 0, 5);
        tvImgLabel.setTextSize(14f);
        layout.addView(tvImgLabel);

        final EditText inImages = new EditText(this);
        inImages.setHint("Ví dụ: ao_thun, ao_thun_den, ao_thun_trang");
        layout.addView(inImages);

        // --- CHỌN DANH MỤC ---
        TextView tvCatLabel = new TextView(this);
        tvCatLabel.setText("Chọn danh mục:");
        tvCatLabel.setPadding(10, 20, 0, 5);
        layout.addView(tvCatLabel);

        final Spinner spnCategory = new Spinner(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(dataAdapter);
        layout.addView(spnCategory);

        builder.setView(layout);

        builder.setPositiveButton("Lưu Sản Phẩm", (dialog, which) -> {
            String name = inName.getText().toString().trim();
            String price = inPrice.getText().toString().trim();
            String oldPrice = inOldPrice.getText().toString().trim();
            String catSelected = spnCategory.getSelectedItem() != null ? spnCategory.getSelectedItem().toString() : "Khác";

            // 1. XỬ LÝ CHUỖI NHẬP VÀO THÀNH DANH SÁCH ẢNH (LIST)
            String imgString = inImages.getText().toString().trim();
            List<String> imagesList = new ArrayList<>();
            if (!imgString.isEmpty()) {
                String[] parts = imgString.split(","); // Tách bằng dấu phẩy
                for (String s : parts) {
                    String cleanName = s.trim();
                    if (!cleanName.isEmpty()) imagesList.add(cleanName);
                }
            }

            if (name.isEmpty() || price.isEmpty() || imagesList.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ Tên, Giá và ít nhất 1 ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Khởi tạo đối tượng Product mới với mảng ảnh
            Product newP = new Product(null, name, price, imagesList, "5.0");
            newP.setOldPrice(oldPrice);
            newP.setCategory(catSelected);

            // 3. Đẩy dữ liệu lên Firestore
            firestore.collection("Product").add(newP).addOnSuccessListener(doc -> {
                newP.setId(doc.getId());
                productList.add(newP);

                // Cập nhật searchList để hiển thị ngay trên GridView
                searchList.add(newP);
                adapter.notifyDataSetChanged();
                updateGridViewHeight();

                Toast.makeText(this, "Đã thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        builder.setNegativeButton("Hủy", null).show();
    }
    private void setupBanner() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.banner2); images.add(R.drawable.banner3); images.add(R.drawable.banner1);
        viewPagerBanner.setAdapter(new BannerAdapter(images));
        viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) { bannerHandler.removeCallbacks(bannerRunnable); bannerHandler.postDelayed(bannerRunnable, 3000); }
        });
    }

    private Runnable bannerRunnable = () -> {
        if (viewPagerBanner.getAdapter() != null) {
            int next = (viewPagerBanner.getCurrentItem() + 1) % viewPagerBanner.getAdapter().getItemCount();
            viewPagerBanner.setCurrentItem(next, true);
        }
    };

    @Override protected void onPause() { super.onPause(); bannerHandler.removeCallbacks(bannerRunnable); }
}