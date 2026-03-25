package com.example.tuantc;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ViewPager2 viewPagerImages;
    RecyclerView rvVariants;
    TextView txtImageCount, txtNameDetail, txtPriceDetail, txtOldPriceDetail, txtDiscountBadge, txtRatingDetail, txtDescription;
    ImageView btnHeart, imgArrowVariant, imgArrowSize;
    Button btnAddToCart;
    CardView btnBack;
    RelativeLayout headerVariant, headerSize;
    LinearLayout layoutSizes;
    TextView selectedSizeView = null;
    RelativeLayout headerDescription, headerCommitment;
    ImageView imgArrowDesc, imgArrowCommit;
    LinearLayout layoutCommitment;

    // --- DATABASE HELPER ---
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_product);

        dbHelper = new DatabaseHelper(this);

        // 1. Ánh xạ các View (Giữ nguyên như cũ)
        viewPagerImages = findViewById(R.id.viewPagerProductImages);
        rvVariants = findViewById(R.id.rvProductVariants);
        txtImageCount = findViewById(R.id.txtImageCount);
        txtNameDetail = findViewById(R.id.txtNameDetail);
        txtPriceDetail = findViewById(R.id.txtPriceDetail);
        txtOldPriceDetail = findViewById(R.id.txtOldPriceDetail);
        txtDiscountBadge = findViewById(R.id.txtDiscountBadge);
        if (txtOldPriceDetail != null) {
            txtOldPriceDetail.setPaintFlags(txtOldPriceDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        txtRatingDetail = findViewById(R.id.txtRatingDetail);
        txtDescription = findViewById(R.id.txtDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBackDetail);
        btnHeart = findViewById(R.id.btnHeart);
        headerVariant = findViewById(R.id.headerVariant);
        headerSize = findViewById(R.id.headerSize);
        imgArrowVariant = findViewById(R.id.imgArrowVariant);
        imgArrowSize = findViewById(R.id.imgArrowSize);
        layoutSizes = findViewById(R.id.layoutSizes);
        headerDescription = findViewById(R.id.headerDescription);
        headerCommitment = findViewById(R.id.headerCommitment);
        imgArrowDesc = findViewById(R.id.imgArrowDesc);
        imgArrowCommit = findViewById(R.id.imgArrowCommit);
        layoutCommitment = findViewById(R.id.layoutCommitment);

        // 2. Nhận dữ liệu Intent
        String id = getIntent().getStringExtra("pId");
        String name = getIntent().getStringExtra("pName");
        String price = getIntent().getStringExtra("pPrice");
        String oldPrice = getIntent().getStringExtra("pOldPrice");
        String rating = getIntent().getStringExtra("pRating");
        ArrayList<String> imageNames = getIntent().getStringArrayListExtra("pImages");

        // --- LẤY THÔNG TIN USER ---
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = pref.getString("current_user", "default_user");

        // 3. Hiển thị thông tin
        if (name != null) {
            txtNameDetail.setText(name);
            txtPriceDetail.setText(price + " VNĐ");
            if (oldPrice != null && !oldPrice.isEmpty()) {
                txtOldPriceDetail.setText(oldPrice + " VNĐ");
                txtOldPriceDetail.setVisibility(View.VISIBLE);
                try {
                    double pNew = Double.parseDouble(price.replace(".", "").replace(",", "").trim());
                    double pOld = Double.parseDouble(oldPrice.replace(".", "").replace(",", "").trim());
                    if (pOld > pNew) {
                        int discountPercent = (int) (100 - (pNew / pOld * 100));
                        txtDiscountBadge.setText("-" + discountPercent + "%");
                        txtDiscountBadge.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {}
            }
            txtDescription.setText("• Chất liệu: Cao cấp\n• Thiết kế: Hiện đại\n• Gia công: Tỉ mỉ.");
            txtRatingDetail.setText((rating != null && !rating.isEmpty()) ? rating + " Rating" : "5.0 Rating");
        }

        // 4, 5. Slider & Click Events
        headerDescription.setOnClickListener(v -> toggleVisibility(txtDescription, imgArrowDesc));
        headerCommitment.setOnClickListener(v -> toggleVisibility(layoutCommitment, imgArrowCommit));
        headerVariant.setOnClickListener(v -> toggleVisibility(rvVariants, imgArrowVariant));
        headerSize.setOnClickListener(v -> toggleVisibility(layoutSizes, imgArrowSize));

        List<Integer> imageResIds = new ArrayList<>();
        if (imageNames != null) {
            for (String img : imageNames) {
                int resId = getResources().getIdentifier(img.trim(), "drawable", getPackageName());
                if (resId != 0) imageResIds.add(resId);
            }
        }
        if (imageResIds.isEmpty()) imageResIds.add(R.drawable.ic_launcher_background);
        viewPagerImages.setAdapter(new ImageSliderAdapter(imageResIds));

        // 6. Xử lý Size
        int[] sizeIds = {R.id.sizeS, R.id.sizeM, R.id.sizeL, R.id.sizeXL};
        for (int sId : sizeIds) {
            TextView tvSize = findViewById(sId);
            if (tvSize != null) {
                tvSize.setOnClickListener(v -> {
                    if (selectedSizeView != null) {
                        selectedSizeView.setBackgroundResource(android.R.drawable.editbox_background);
                        selectedSizeView.setTextColor(Color.BLACK);
                    }
                    tvSize.setBackgroundColor(Color.parseColor("#1E293B"));
                    tvSize.setTextColor(Color.WHITE);
                    selectedSizeView = tvSize;
                });
            }
        }

        btnBack.setOnClickListener(v -> finish());

        // --- XỬ LÝ YÊU THÍCH ---
        boolean isAlreadyFav = false;
        if (FavoriteActivity.favList != null) {
            for (Product prd : FavoriteActivity.favList) {
                if (prd.getId() != null && prd.getId().equals(id)) {
                    isAlreadyFav = true;
                    break;
                }
            }
        }
        btnHeart.setImageTintList(ColorStateList.valueOf(isAlreadyFav ? Color.RED : Color.parseColor("#1E293B")));

        btnHeart.setOnClickListener(v -> {
            int index = -1;
            for (int i = 0; i < FavoriteActivity.favList.size(); i++) {
                if (FavoriteActivity.favList.get(i).getId().equals(id)) {
                    index = i; break;
                }
            }
            if (index != -1) {
                FavoriteActivity.favList.remove(index);
                btnHeart.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1E293B")));
            } else {
                Product favP = new Product();
                favP.setId(id); favP.setName(name); favP.setPrice(price);
                favP.setImages(imageNames); favP.setRating(rating);
                FavoriteActivity.favList.add(favP);
                btnHeart.setImageTintList(ColorStateList.valueOf(Color.RED));
            }
            // LƯU VÀO SQLITE
            dbHelper.saveFavoriteList(currentUser, FavoriteActivity.favList);
        });

        // --- THÊM VÀO GIỎ HÀNG ---
        btnAddToCart.setOnClickListener(v -> {
            if (selectedSizeView == null) {
                Toast.makeText(this, "Vui lòng chọn Size!", Toast.LENGTH_SHORT).show();
                return;
            }
            String selectedSize = selectedSizeView.getText().toString();
            Product cartProduct = new Product();
            cartProduct.setId(id); cartProduct.setName(name); cartProduct.setPrice(price);
            cartProduct.setImages(imageNames); cartProduct.setSize(selectedSize);
            cartProduct.setQuantity(1); cartProduct.setRating(rating);

            boolean isExist = false;
            for (Product prd : CartActivity.cartList) {
                if (prd.getId().equals(id) && prd.getSize().equals(selectedSize)) {
                    prd.setQuantity(prd.getQuantity() + 1);
                    isExist = true; break;
                }
            }
            if (!isExist) CartActivity.cartList.add(cartProduct);

            // LƯU VÀO SQLITE
            dbHelper.saveCartList(currentUser, CartActivity.cartList);
            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        renderComments();
    }

    private void toggleVisibility(View content, ImageView arrow) {
        if (content.getVisibility() == View.GONE) {
            content.setVisibility(View.VISIBLE);
            arrow.animate().rotation(180f).setDuration(200).start();
        } else {
            content.setVisibility(View.GONE);
            arrow.animate().rotation(0f).setDuration(200).start();
        }
    }

    private void renderComments() {
        LinearLayout layoutComments = findViewById(R.id.listCommentContainer);
        if (layoutComments == null) return;
        layoutComments.removeAllViews();

        String[] users = {"Nguyễn Hoàng Nam", "Lê Thu Thảo", "Trần Minh Tâm"};
        String[] contents = {
                "Sản phẩm rất đẹp, đóng gói kỹ càng. Giao hàng nhanh hơn dự kiến!",
                "Chất lượng vải tốt, đúng mô tả. Sẽ quay lại ủng hộ.",
                "Giá cả hợp lý, form dáng chuẩn đẹp. Rất hài lòng!"
        };

        String[][] reviewImages = {
                {"aothun01", "aothun02"},
                {"giay_da_nau", "giay_da_den"},
                {"thatlung"}
        };

        for (int i = 0; i < users.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.item_comment_vip, null);
            TextView name = view.findViewById(R.id.txtUser);
            TextView content = view.findViewById(R.id.txtContent);
            LinearLayout imgLayout = view.findViewById(R.id.layoutReviewImages);

            name.setText(users[i]);
            content.setText(contents[i]);

            for (String imgName : reviewImages[i]) {
                int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
                if (resId != 0) {
                    ImageView img = new ImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, 180);
                    lp.setMargins(0, 0, 15, 0);
                    img.setLayoutParams(lp);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img.setImageResource(resId);
                    img.setClipToOutline(true);
                    img.setBackgroundResource(R.drawable.bg_image_count);
                    imgLayout.addView(img);
                }
            }
            layoutComments.addView(view);

            View divider = new View(this);
            divider.setBackgroundColor(Color.parseColor("#E2E8F0"));
            layoutComments.addView(divider, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
        }
    }
}