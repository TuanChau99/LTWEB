package com.example.tuantc;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.ViewHolder> {
    private List<Integer> images;
    private int selectedPosition = 0;
    private OnVariantClickListener listener;

    public interface OnVariantClickListener {
        void onVariantClick(int position);
    }

    public VariantAdapter(List<Integer> images, OnVariantClickListener listener) {
        this.images = images;
        this.listener = listener;
    }

    public void setSelectedPosition(int position) {
        int oldPos = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(oldPos);
        notifyItemChanged(selectedPosition);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_variant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // holder.imgVariant.setImageResource(images.get(position));

        // Dùng Glide để load ảnh nhỏ (Thumbnail)
        com.bumptech.glide.Glide.with(holder.itemView.getContext())
                .load(images.get(position))
                .override(150, 150) // Ép ảnh về kích thước 150x150 pixel để máy chạy cực nhẹ
                .centerCrop()
                .into(holder.imgVariant);

        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.bg_selected_variant);
        } else {
            holder.itemView.setBackgroundResource(android.R.drawable.editbox_background);
        }

        holder.itemView.setOnClickListener(v -> {
            setSelectedPosition(position);
            listener.onVariantClick(position);
        });
    }

    @Override
    public int getItemCount() { return images.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgVariant;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgVariant = itemView.findViewById(R.id.imgVariantThumb);
        }
    }
}