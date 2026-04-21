package com.example.tuantc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.ViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private OnCategoryClickListener listener;

    // Interface để xử lý sự kiện click từ Activity
    public interface OnCategoryClickListener {
        void onEditClick(Category category);
        void onDeleteClick(Category category);
    }

    public AdminCategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);

        holder.tvCategoryName.setText(category.getName());
        holder.tvCategoryId.setText("ID: " + (category.getId() != null ? category.getId().substring(0, 5) : "N/A"));

        // Lấy ảnh từ drawable dựa trên tên lưu trong Firestore
        int resId = context.getResources().getIdentifier(category.getImage(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.imgCategoryIcon.setImageResource(resId);
        } else {
            holder.imgCategoryIcon.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Sự kiện nút Sửa
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(category));

        // Sự kiện nút Xóa
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(category));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgCategoryIcon;
        TextView tvCategoryName, tvCategoryId;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategoryIcon = itemView.findViewById(R.id.imgCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryId = itemView.findViewById(R.id.tvCategoryId);
            btnEdit = itemView.findViewById(R.id.btnEditCat);
            btnDelete = itemView.findViewById(R.id.btnDeleteCat);
        }
    }
}