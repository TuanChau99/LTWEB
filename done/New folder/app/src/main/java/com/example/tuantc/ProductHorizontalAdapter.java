package com.example.tuantc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductHorizontalAdapter extends RecyclerView.Adapter<ProductHorizontalAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductHorizontalAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // 1. Hiển thị ảnh sử dụng hàm getPrimaryImage để lấy ảnh đầu tiên trong mảng
        int resId = context.getResources().getIdentifier(product.getPrimaryImage(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.imgProduct.setImageResource(resId);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }

        // 2. CẬP NHẬT: Gửi đầy đủ mảng ảnh sang DetailActivity để hiển thị ViewPager
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("pId", product.getId());
            intent.putExtra("pName", product.getName());
            intent.putExtra("pPrice", product.getPrice());
            intent.putExtra("pRating", product.getRating());

            // QUAN TRỌNG: Gửi mảng pImages thay vì pImage đơn lẻ
            if (product.getImages() != null) {
                intent.putStringArrayListExtra("pImages", new ArrayList<>(product.getImages()));
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductHorizontal);
        }
    }
}