package com.example.tuantc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public AdminProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(p.getPrice() + "đ");

        // Load ảnh đầu tiên trong danh sách
        if (p.getImages() != null && !p.getImages().isEmpty()) {
            int resId = context.getResources().getIdentifier(p.getImages().get(0), "drawable", context.getPackageName());
            holder.imgProduct.setImageResource(resId != 0 ? resId : android.R.drawable.ic_menu_gallery);
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(p));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(p));
    }

    @Override
    public int getItemCount() { return productList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnEdit, btnDelete;
        TextView tvName, tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgAdminProduct);
            tvName = itemView.findViewById(R.id.tvAdminProductName);
            tvPrice = itemView.findViewById(R.id.tvAdminProductPrice);
            btnEdit = itemView.findViewById(R.id.btnEditProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }
}