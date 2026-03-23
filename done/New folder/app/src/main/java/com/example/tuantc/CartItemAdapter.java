package com.example.tuantc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CartItemAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> list;

    public CartItemAdapter(Context context, List<CartItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list != null ? list.size() : 0; }
    @Override
    public Object getItem(int i) { return list.get(i); }
    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, viewGroup, false);
        }

        CartItem item = list.get(i);

        // Ánh xạ các View từ file item_cart_product.xml
        ImageView imgProduct = view.findViewById(R.id.imgItemProduct);
        TextView tvName = view.findViewById(R.id.tvItemName);
        TextView tvSize = view.findViewById(R.id.tvItemSize);
        TextView tvPriceQty = view.findViewById(R.id.tvItemPriceQuantity);

        tvName.setText(item.getName());
        tvSize.setText("Phân loại: " + item.getSize());
        tvPriceQty.setText(item.getPrice() + " x" + item.getQuantity());

        // LOGIC LOAD ẢNH TỪ DRAWABLE QUA TÊN FILE
        String imageName = item.getImage(); // Ví dụ: "aothun01"
        if (imageName != null && !imageName.isEmpty()) {
            // Tìm ID của ảnh trong thư mục drawable
            int resId = context.getResources().getIdentifier(imageName.trim(), "drawable", context.getPackageName());
            if (resId != 0) {
                imgProduct.setImageResource(resId);
            } else {
                imgProduct.setImageResource(R.drawable.ic_launcher_background); // Ảnh mặc định nếu lỗi
            }
        }

        return view;
    }
}