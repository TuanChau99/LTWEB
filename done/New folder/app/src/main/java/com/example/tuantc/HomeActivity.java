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
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    ImageView btnSearchHeader, btnNotification;
    GridView gvProducts;
    TextView tvSeeAll, tvSearchTitle, tvAddCategory;
    LinearLayout layoutHomeContent, layoutCategories;
    List<Product> productList, searchList;
    ProductAdapter adapter;

    RecyclerView rvNewProducts, rvBestSellers, rvVouchers;
    List<Voucher> voucherList;
    VoucherAdapter voucherAdapter;
    List<Product> newProductsList, bestSellersList;
    ProductHorizontalAdapter newAdapter, bestAdapter;

    LinearLayout navContainer;
    FloatingActionButton fabAddProduct;
    private FirebaseFirestore firestore;
    ViewPager2 viewPagerBanner;
    private Handler bannerHandler = new Handler(Looper.getMainLooper());

    // Mặc định Home là giao diện khách
    private boolean isAdmin = false;

    List<String> categoryNames = new ArrayList<>();
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "");

        FavoriteActivity.favList = new ArrayList<>(dbHelper.getFavoriteList(currentUser));
        CartActivity.cartList = new ArrayList<>(dbHelper.getCartList(currentUser));

        isAdmin = false;

        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        newProductsList = new ArrayList<>();
        bestSellersList = new ArrayList<>();

        initViews();
        setupRecyclerViews();
        setupNavigation();

        firestore = FirebaseFirestore.getInstance();

        // Luôn ẩn các nút quản lý tại trang Home của khách
        if (fabAddProduct != null) fabAddProduct.setVisibility(View.GONE);
        if (tvAddCategory != null) tvAddCategory.setVisibility(View.GONE);

        loadCategoriesFromFirestore();
        setupBanner();
        loadProductsFromFirestore();
        loadVouchersFromFirestore();

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

        gvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product selected = searchList.get(position);
            goToDetail(selected);
        });
    }

    private void initViews() {
        btnSearchHeader = findViewById(R.id.btnSearchHeader);
        btnNotification = findViewById(R.id.btnNotification);
        tvSearchTitle = findViewById(R.id.tvSearchTitle);
        viewPagerBanner = findViewById(R.id.viewPagerBanner);
        layoutCategories = findViewById(R.id.layoutCategories);
        tvAddCategory = findViewById(R.id.tvAddCategory);
        layoutHomeContent = findViewById(R.id.layoutHomeContent);
        rvNewProducts = findViewById(R.id.rvNewProducts);
        rvBestSellers = findViewById(R.id.rvBestSellers);
        gvProducts = findViewById(R.id.gvProducts);
        tvSeeAll = findViewById(R.id.tvSeeAll);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        navContainer = findViewById(R.id.layoutBottomNavContainer);
        rvVouchers = findViewById(R.id.rvVouchers);
    }

    private void setupNavigation() {
        if (navContainer == null) return;
        navContainer.removeAllViews();

        // Chỉ hiện Menu dành cho khách hàng
        addNavItem("Trang chủ", android.R.drawable.ic_menu_today, HomeActivity.class);
        addNavItem("Yêu thích", R.drawable.heart, FavoriteActivity.class);
        addNavItem("Giỏ hàng", android.R.drawable.ic_menu_directions, CartActivity.class);
        addNavItem("Đơn hàng", android.R.drawable.ic_menu_recent_history, OrderHistoryActivity.class);
        addNavItem("Cá nhân", android.R.drawable.ic_menu_myplaces, ProfileActivity.class);
    }

    private void addNavItem(String title, int iconRes, Class<?> targetActivity) {
        View itemView = getLayoutInflater().inflate(R.layout.item_nav_menu, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        itemView.setLayoutParams(params);

        TextView tv = itemView.findViewById(R.id.tvNavTitle);
        ImageView img = itemView.findViewById(R.id.imgNavIcon);

        tv.setText(title);
        img.setImageResource(iconRes);

        if (this.getClass().getSimpleName().equals(targetActivity.getSimpleName())) {
            tv.setTextColor(Color.parseColor("#10B981"));
            img.setColorFilter(Color.parseColor("#10B981"));
        }

        itemView.setOnClickListener(v -> {
            if (this.getClass() != targetActivity) {
                Intent intent = new Intent(this, targetActivity);
                intent.putExtra("isAdmin", false);
                startActivity(intent);
            }
        });

        navContainer.addView(itemView);
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

    private void addNewCategoryToUI(String id, String name, String imgName) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 40, 0);
        item.setLayoutParams(params);

        ShapeableImageView img = new ShapeableImageView(this);
        int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
        img.setImageResource(resId != 0 ? resId : android.R.drawable.ic_menu_gallery);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        img.setStrokeColor(getColorStateList(android.R.color.darker_gray));
        img.setStrokeWidth(2f);
        img.setShapeAppearanceModel(ShapeAppearanceModel.builder()
                .setAllCornerSizes(ShapeAppearanceModel.PILL).build());

        TextView txt = new TextView(this);
        txt.setText(name);
        txt.setTextSize(12f);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(0, 10, 0, 0);

        item.addView(img);
        item.addView(txt);

        item.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("CATEGORY_NAME", name);
            startActivity(intent);
        });

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

    private void setupRecyclerViews() {
        rvNewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvBestSellers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newAdapter = new ProductHorizontalAdapter(this, newProductsList);
        bestAdapter = new ProductHorizontalAdapter(this, bestSellersList);
        rvNewProducts.setAdapter(newAdapter);
        rvBestSellers.setAdapter(bestAdapter);

        // Adapter cho khách hàng luôn để isAdmin = false
        adapter = new ProductAdapter(this, searchList, false, false);
        gvProducts.setAdapter(adapter);

        rvVouchers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        voucherList = new ArrayList<>();
        voucherAdapter = new VoucherAdapter(this, voucherList);

        voucherAdapter.setOnVoucherClickListener(voucher -> {
            SharedPreferences pref = getSharedPreferences("VoucherPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("applied_code", voucher.getCode());
            editor.apply();
            Toast.makeText(this, "Đã lưu mã: " + voucher.getCode(), Toast.LENGTH_SHORT).show();
        });

        rvVouchers.setAdapter(voucherAdapter);
    }

    private void loadVouchersFromFirestore() {
        firestore.collection("Vouchers").get().addOnSuccessListener(snapshots -> {
            voucherList.clear();
            if (!snapshots.isEmpty()) {
                for (DocumentSnapshot doc : snapshots) {
                    Voucher v = doc.toObject(Voucher.class);
                    if (v != null) voucherList.add(v);
                }
            }
            voucherAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.e("ERROR", "Lỗi: " + e.getMessage()));
    }

    private void goToDetail(Product product) {
        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        intent.putExtra("pId", product.getId());
        intent.putExtra("pName", product.getName());
        intent.putExtra("pPrice", product.getPrice());
        intent.putExtra("pOldPrice", product.getOldPrice());
        intent.putExtra("pRating", product.getRating());
        if (product.getImages() != null) {
            intent.putStringArrayListExtra("pImages", new ArrayList<>(product.getImages()));
        } else {
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

    private void setupBanner() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.banner2); images.add(R.drawable.banner3); images.add(R.drawable.banner1);
        viewPagerBanner.setAdapter(new BannerAdapter(images));
        viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                bannerHandler.removeCallbacks(bannerRunnable);
                bannerHandler.postDelayed(bannerRunnable, 3000);
            }
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