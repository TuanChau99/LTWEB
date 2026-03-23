package com.example.tuantc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private boolean isCart;
    private boolean isAdmin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userEmail = "tuantc@vimenstore.com";

    // Cập nhật Constructor để nhận thêm biến isAdmin
    public ProductAdapter(Context context, List<Product> productList, boolean isCart, boolean isAdmin) {
        this.context = context;
        this.productList = productList;
        this.isCart = isCart;
        this.isAdmin = isAdmin;
    }

    @Override
    public int getCount() { return productList.size(); }
    @Override
    public Object getItem(int i) { return productList.get(i); }
    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Product p = productList.get(i);

        // --- PHẦN 1: XỬ LÝ CHO TRANG LỊCH SỬ ĐƠN HÀNG ---
        if (p.getOrderId() != null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_product_adapter, viewGroup, false);
            TextView txtName = view.findViewById(R.id.txtNameProductAdapter);
            TextView txtPrice = view.findViewById(R.id.tvPriceAdapter);
            ImageView img = view.findViewById(R.id.imgProductAdapter);
            TextView tvStatus = view.findViewById(R.id.tvStatusAdapter);
            TextView txtQty = view.findViewById(R.id.tvQuantityAdapter);
            Button btnCancel = view.findViewById(R.id.btnCancelOrderAdapter);
            Button btnAction = view.findViewById(R.id.btnBuyAgainAdapter);

            txtName.setText(p.getName());
            txtPrice.setText(p.getPrice() + " VNĐ");
            txtQty.setText("x" + p.getQuantity());
            tvStatus.setText(p.getStatus());

            int imageId = context.getResources().getIdentifier(p.getPrimaryImage(), "drawable", context.getPackageName());
            img.setImageResource(imageId != 0 ? imageId : R.drawable.ic_launcher_background);

            // LOGIC PHÂN QUYỀN NÚT BẤM
            if (isAdmin) {
                // Giao diện cho Admin
                if ("Đang giao".equals(p.getStatus())) {
                    tvStatus.setTextColor(Color.parseColor("#EE4D2D"));
                    btnCancel.setVisibility(View.VISIBLE);
                    btnCancel.setText("Hủy đơn");
                    btnAction.setVisibility(View.VISIBLE);
                    btnAction.setText("Xác nhận giao");

                    btnAction.setOnClickListener(v -> updateStatusOnFirebase(p, "Hoàn thành"));
                    btnCancel.setOnClickListener(v -> showCancelDialog(p));
                } else {
                    tvStatus.setTextColor(Color.parseColor("#94A3B8"));
                    btnCancel.setVisibility(View.GONE);
                    btnAction.setVisibility(View.GONE);
                }
            } else {
                // Giao diện cho User (Khách hàng)
                if ("Đang giao".equals(p.getStatus())) {
                    tvStatus.setTextColor(Color.parseColor("#EE4D2D"));
                    btnCancel.setVisibility(View.VISIBLE);
                    btnCancel.setText("Hủy đơn");
                    btnAction.setVisibility(View.GONE);

                    btnCancel.setOnClickListener(v -> showCancelDialog(p));
                } else {
                    tvStatus.setTextColor(Color.parseColor("#94A3B8"));
                    btnCancel.setVisibility(View.GONE);
                    btnAction.setVisibility(View.VISIBLE);
                    btnAction.setText("Mua lại");

                    btnAction.setOnClickListener(v -> {
                        Toast.makeText(context, "Đã thêm " + p.getName() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
            return view;
        }

        // --- PHẦN 2: XỬ LÝ CHO TRANG CHỦ VÀ GIỎ HÀNG ---
        int layoutId = isCart ? R.layout.cart_item : R.layout.product_item;
        view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);

        ImageView img = view.findViewById(R.id.imgProduct);
        TextView txtName = view.findViewById(R.id.txtProductName);
        TextView txtPrice = view.findViewById(R.id.txtProductPrice);
        TextView txtRating = view.findViewById(R.id.txtRating);
        TextView txtOldPrice = view.findViewById(R.id.txtProductOldPrice);

        if (p != null) {
            txtName.setText(isCart ? p.getName() : p.getName().toUpperCase());
            txtPrice.setText(p.getPrice() + " VNĐ");

            if (txtOldPrice != null) {
                if (p.getOldPrice() != null && !p.getOldPrice().isEmpty()) {
                    txtOldPrice.setText(p.getOldPrice() + " VNĐ");
                    txtOldPrice.setPaintFlags(txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    txtOldPrice.setVisibility(View.VISIBLE);
                } else {
                    txtOldPrice.setVisibility(View.GONE);
                }
            }

            if (txtRating != null) {
                txtRating.setText(p.getRating() != null ? p.getRating() : "5.0");
            }

            int imageId = context.getResources().getIdentifier(p.getPrimaryImage(), "drawable", context.getPackageName());
            img.setImageResource(imageId != 0 ? imageId : R.drawable.ic_launcher_background);

            if (isCart) {
                TextView txtQuantity = view.findViewById(R.id.txtQuantity);
                TextView btnPlus = view.findViewById(R.id.btnPlus);
                TextView btnMinus = view.findViewById(R.id.btnMinus);
                ImageView btnDelete = view.findViewById(R.id.btnDelete);
                View layoutQuantity = view.findViewById(R.id.layoutQuantity);

                if (layoutQuantity != null) layoutQuantity.setVisibility(View.VISIBLE);
                if (btnDelete != null) btnDelete.setVisibility(View.VISIBLE);
                if (txtQuantity != null) txtQuantity.setText(String.valueOf(p.getQuantity()));

                if (btnPlus != null) {
                    btnPlus.setOnClickListener(v -> {
                        p.setQuantity(p.getQuantity() + 1);
                        notifyDataSetChanged();
                        if (context instanceof CartActivity) ((CartActivity) context).updateTotal();
                    });
                }
                if (btnMinus != null) {
                    btnMinus.setOnClickListener(v -> {
                        if (p.getQuantity() > 1) {
                            p.setQuantity(p.getQuantity() - 1);
                            notifyDataSetChanged();
                            if (context instanceof CartActivity) ((CartActivity) context).updateTotal();
                        }
                    });
                }
                if (btnDelete != null) {
                    btnDelete.setOnClickListener(v -> {
                        new AlertDialog.Builder(context)
                                .setTitle("Xác nhận xóa")
                                .setMessage("Bạn có chắc chắn muốn xóa " + p.getName() + " khỏi giỏ hàng không?")
                                .setPositiveButton("Xóa", (dialog, which) -> {
                                    productList.remove(i);
                                    notifyDataSetChanged();
                                    if (context instanceof CartActivity) ((CartActivity) context).updateTotal();
                                })
                                .setNegativeButton("Hủy", null)
                                .show();
                    });
                }
            }
        }
        return view;
    }

    // Hàm cập nhật trạng thái đơn hàng lên Firestore
    private void updateStatusOnFirebase(Product p, String newStatus) {
        db.collection("Orders").document(userEmail)
                .collection("user_orders")
                .whereEqualTo("orderId", p.getOrderId())
                .whereEqualTo("name", p.getName())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String docId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("Orders").document(userEmail)
                                .collection("user_orders").document(docId)
                                .update("status", newStatus)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(context, "Đã cập nhật đơn hàng!", Toast.LENGTH_SHORT).show());
                    }
                });
    }

    // Hàm hiển thị Dialog xác nhận hủy đơn
    private void showCancelDialog(Product p) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận hủy đơn")
                .setMessage("Bạn có chắc chắn muốn hủy sản phẩm này không?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> updateStatusOnFirebase(p, "Đã hủy"))
                .setNegativeButton("Quay lại", null)
                .show();
    }
}