package com.example.tuantc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Cần thêm 2 dòng import này để fix lỗi ArrayList và List
import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductRecyclerAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout product_item dành cho item trong danh sách
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        if (p == null) return;

        // Hiển thị tên (Viết hoa) và giá
        holder.txtName.setText(p.getName() != null ? p.getName().toUpperCase() : "SẢN PHẨM");
        holder.txtPrice.setText((p.getPrice() != null ? p.getPrice() : "0") + " VNĐ");

        if (holder.txtRating != null) {
            holder.txtRating.setText(p.getRating() != null ? p.getRating() : "5.0");
        }

        // Load ảnh từ thư mục drawable dựa vào tên file lưu trong DB
        int imageId = 0;
        if (p.getPrimaryImage() != null) {
            imageId = context.getResources().getIdentifier(p.getPrimaryImage().trim(), "drawable", context.getPackageName());
        }
        holder.imgProduct.setImageResource(imageId != 0 ? imageId : R.drawable.ic_launcher_background);

        // Sự kiện click để xem chi tiết sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("pId", p.getId());
            intent.putExtra("pName", p.getName());
            intent.putExtra("pPrice", p.getPrice());
            intent.putExtra("pRating", p.getRating());

            // Fix lỗi chuyển đổi List sang ArrayList và kiểm tra null
            if (p.getImages() != null) {
                intent.putStringArrayListExtra("pImages", new ArrayList<>(p.getImages()));
            } else {
                intent.putStringArrayListExtra("pImages", new ArrayList<>());
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtRating;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtRating = itemView.findViewById(R.id.txtRating);
        }
    }
}