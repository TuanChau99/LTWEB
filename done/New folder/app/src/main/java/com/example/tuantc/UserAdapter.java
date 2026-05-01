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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;
    private OnUserClickListener listener;

    // Interface để xử lý sự kiện click từ Activity
    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item_user (bạn đã tạo ở bước trước)
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) return;

        holder.tvUsername.setText("Username: " + user.getUsername());
        holder.tvPassword.setText("Password: " + user.getPassword());
        holder.tvRules.setText("Rules: " + user.getRules());

        // Sự kiện khi nhấn vào nút sửa (hình cây bút)
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onUserClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvPassword, tvRules;
        ImageView btnEdit, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvRules = itemView.findViewById(R.id.tvRules);
            btnEdit = itemView.findViewById(R.id.btnEditUser);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}