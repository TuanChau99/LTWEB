package com.example.tuantc;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUserActivity extends AppCompatActivity {
    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private List<User> userList;
    private FirebaseFirestore db;
    private FloatingActionButton fabAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        db = FirebaseFirestore.getInstance();
        rvUsers = findViewById(R.id.rvUserList);
        fabAddUser = findViewById(R.id.fabAddUser);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        userList = new ArrayList<>();
        // Khởi tạo Adapter (Giả định bạn đã có UserAdapter)
        adapter = new UserAdapter(this, userList);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        // Click để sửa/xóa
        adapter.setOnUserClickListener(user -> showEditUserDialog(user));

        fabAddUser.setOnClickListener(v -> showAddUserDialog());

        loadUsersFromFirestore();
    }

    private void loadUsersFromFirestore() {
        db.collection("Users").get().addOnSuccessListener(snapshots -> {
            userList.clear();
            for (DocumentSnapshot doc : snapshots) {
                User u = doc.toObject(User.class);
                if (u != null) {
                    u.setId(doc.getId()); // Lưu ID document để dễ update/delete
                    userList.add(u);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void showEditUserDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_user, null);
        builder.setView(view);

        EditText edtUser = view.findViewById(R.id.edtDialogUser);
        EditText edtPass = view.findViewById(R.id.edtDialogPass);
        RadioButton rbAdmin = view.findViewById(R.id.rbAdmin);
        RadioButton rbUser = view.findViewById(R.id.rbUser);
        TextView btnLuu = view.findViewById(R.id.btnSave);
        TextView btnHuy = view.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();

        // Đổ dữ liệu cũ vào dialog
        edtUser.setText(user.getUsername());
        edtPass.setText(user.getPassword());
        if ("admin".equals(user.getRules())) rbAdmin.setChecked(true); else rbUser.setChecked(true);

        btnLuu.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("username", edtUser.getText().toString().trim());
            map.put("password", edtPass.getText().toString().trim());
            map.put("rules", rbAdmin.isChecked() ? "admin" : "user");

            db.collection("Users").document(user.getId()).update(map)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        loadUsersFromFirestore();
                        dialog.dismiss();
                    });
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_user, null);
        builder.setView(view);

        // 2. Ánh xạ các View từ file xml của Dialog
        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        EditText edtUser = view.findViewById(R.id.edtDialogUser);
        EditText edtPass = view.findViewById(R.id.edtDialogPass);
        RadioButton rbAdmin = view.findViewById(R.id.rbAdmin);
        RadioButton rbUser = view.findViewById(R.id.rbUser);
        TextView btnLuu = view.findViewById(R.id.btnSave);
        TextView btnHuy = view.findViewById(R.id.btnCancel);

        if (tvTitle != null) {
            tvTitle.setText("THÊM TÀI KHOẢN MỚI");
        }

        AlertDialog dialog = builder.create();
        // Làm nền dialog trong suốt nếu bạn có bo góc trong XML
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        btnLuu.setOnClickListener(v -> {
            String username = edtUser.getText().toString().trim();
            String password = edtPass.getText().toString().trim();
            String rules = rbAdmin.isChecked() ? "admin" : "user";

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ username và password!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. Đẩy dữ liệu lên Firestore
            Map<String, Object> newUser = new HashMap<>();
            newUser.put("username", username);
            newUser.put("password", password);
            newUser.put("rules", rules);
            newUser.put("fullName", username);
            newUser.put("phone", "");

            btnLuu.setEnabled(false); // Ngăn bấm nhiều lần gây trùng lặp

            db.collection("Users")
                    .add(newUser)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Đã thêm Admin/User mới thành công!", Toast.LENGTH_SHORT).show();
                        loadUsersFromFirestore();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        btnLuu.setEnabled(true);
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}