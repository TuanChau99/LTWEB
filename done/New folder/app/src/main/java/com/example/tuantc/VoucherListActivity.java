package com.example.tuantc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class VoucherListActivity extends AppCompatActivity {
    private RecyclerView rvVoucherList;
    private VoucherAdapter adapter;
    private List<Voucher> voucherList;
    private FirebaseFirestore db;
    private FloatingActionButton fabAddVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);

        db = FirebaseFirestore.getInstance();
        rvVoucherList = findViewById(R.id.rvVoucherList);
        fabAddVoucher = findViewById(R.id.fabAddVoucher);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        voucherList = new ArrayList<>();
        adapter = new VoucherAdapter(this, voucherList);

        rvVoucherList.setLayoutManager(new LinearLayoutManager(this));
        rvVoucherList.setAdapter(adapter);

        // Sự kiện khi Click vào một Voucher để Sửa hoặc Xóa
        adapter.setOnVoucherClickListener(voucher -> {
            showEditVoucherDialog(voucher);
        });

        // Sự kiện khi nhấn nút Thêm (+)
        fabAddVoucher.setOnClickListener(v -> showAddVoucherDialog());

        loadVouchers();
    }

    private void loadVouchers() {
        db.collection("Vouchers").get().addOnSuccessListener(snapshots -> {
            voucherList.clear();
            for (DocumentSnapshot doc : snapshots) {
                Voucher v = doc.toObject(Voucher.class);
                if (v != null) {
                    voucherList.add(v);
                }
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show());
    }

    private void showAddVoucherDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_product, null);
        builder.setView(view);

        EditText edtCode = view.findViewById(R.id.edtEditProductName);
        EditText edtDiscount = view.findViewById(R.id.edtEditProductPrice);
        EditText edtDesc = view.findViewById(R.id.edtEditProductDescription);

        // Ẩn các thành phần không liên quan của layout cũ
        view.findViewById(R.id.edtEditProductImages).setVisibility(View.GONE);
        view.findViewById(R.id.imgEditPreview).setVisibility(View.GONE);
        view.findViewById(R.id.btnSelectImage).setVisibility(View.GONE);
        view.findViewById(R.id.spnEditCategory).setVisibility(View.GONE);

        edtCode.setHint("Mã Voucher (vd: VIMEN20)");
        edtDiscount.setHint("Phần trăm giảm (vd: 20)");
        edtDesc.setHint("Mô tả voucher");

        builder.setPositiveButton("THÊM MỚI", (dialog, which) -> {
            String code = edtCode.getText().toString().trim();
            String discount = edtDiscount.getText().toString().trim();
            String desc = edtDesc.getText().toString().trim();

            if (code.isEmpty() || discount.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Voucher newV = new Voucher(code, discount, desc);
            db.collection("Vouchers").add(newV).addOnSuccessListener(doc -> {
                Toast.makeText(this, "Thêm voucher thành công!", Toast.LENGTH_SHORT).show();
                loadVouchers();
            });
        });
        builder.setNegativeButton("HỦY", null);
        builder.show();
    }

    private void showEditVoucherDialog(Voucher voucher) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_product, null);
        builder.setView(view);

        EditText edtCode = view.findViewById(R.id.edtEditProductName);
        EditText edtDiscount = view.findViewById(R.id.edtEditProductPrice);
        EditText edtDesc = view.findViewById(R.id.edtEditProductDescription);

        view.findViewById(R.id.edtEditProductImages).setVisibility(View.GONE);
        view.findViewById(R.id.imgEditPreview).setVisibility(View.GONE);
        view.findViewById(R.id.btnSelectImage).setVisibility(View.GONE);
        view.findViewById(R.id.spnEditCategory).setVisibility(View.GONE);

        edtCode.setText(voucher.getCode());
        edtDiscount.setText(voucher.getDiscount());
        edtDesc.setText(voucher.getDescription());

        builder.setPositiveButton("CẬP NHẬT", (dialog, which) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", edtCode.getText().toString().trim());
            map.put("discount", edtDiscount.getText().toString().trim());
            map.put("description", edtDesc.getText().toString().trim());

            // Tìm document có mã tương ứng để update
            db.collection("Vouchers").whereEqualTo("code", voucher.getCode()).get().addOnSuccessListener(query -> {
                if (!query.isEmpty()) {
                    String docId = query.getDocuments().get(0).getId();
                    db.collection("Vouchers").document(docId).update(map).addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
                        loadVouchers();
                    });
                }
            });
        });

        builder.setNeutralButton("XÓA", (dialog, which) -> {
            db.collection("Vouchers").whereEqualTo("code", voucher.getCode()).get().addOnSuccessListener(query -> {
                if (!query.isEmpty()) {
                    String docId = query.getDocuments().get(0).getId();
                    db.collection("Vouchers").document(docId).delete().addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Đã xóa voucher!", Toast.LENGTH_SHORT).show();
                        loadVouchers();
                    });
                }
            });
        });

        builder.setNegativeButton("HỦY", null);
        builder.show();
    }
}