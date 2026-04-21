package com.example.tuantc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {
    private Context context;
    private List<Voucher> voucherList;
    private OnVoucherClickListener listener;

    // Định nghĩa Interface
    public interface OnVoucherClickListener {
        void onVoucherClick(Voucher voucher);
    }

    public void setOnVoucherClickListener(OnVoucherClickListener listener) {
        this.listener = listener;
    }

    public VoucherAdapter(Context context, List<Voucher> voucherList) {
        this.context = context;
        this.voucherList = voucherList;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher v = voucherList.get(position);
        holder.tvCode.setText(v.getCode());
        holder.tvDesc.setText(v.getDescription());
        holder.tvExpiry.setText("HSD: " + v.getExpiryDate());

        // Bắt sự kiện click vào item
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onVoucherClick(v);
            }
        });
    }

    @Override
    public int getItemCount() { return voucherList.size(); }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvDesc, tvExpiry;
        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvVoucherCode);
            tvDesc = itemView.findViewById(R.id.tvVoucherDesc);
            tvExpiry = itemView.findViewById(R.id.tvVoucherExpiry);
        }
    }
}