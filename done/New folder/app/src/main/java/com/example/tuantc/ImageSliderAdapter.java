package com.example.tuantc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ViewHolder> {
    private List<Integer> images;

    public ImageSliderAdapter(List<Integer> images) { this.images = images; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // holder.imageView.setImageResource(images.get(position));

        // Dùng Glide để load ảnh thông minh
        com.bumptech.glide.Glide.with(holder.itemView.getContext())
                .load(images.get(position))
                .centerCrop() // Cắt ảnh cho vừa khít khung hình
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() { return images.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewSlider);
        }
    }
}