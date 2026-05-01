package com.example.tuantc;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    private boolean isAdmin = false; // Biến chốt quyền

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);

        // 1. NHẬN QUYỀN TỪ INTENT
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        db = FirebaseFirestore.getInstance();
        rvVoucherList = findViewById(R.id.rvVoucherList);
        fabAddVoucher = findViewById(R.id.fabAddVoucher);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        // 2. ẨN/HIỆN NÚT THÊM (+) DỰA TRÊN QUYỀN
        fabAddVoucher.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

        voucherList = new ArrayList<>();
        adapter = new VoucherAdapter(this, voucherList);
        rvVoucherList.setLayoutManager(new LinearLayoutManager(this));
        rvVoucherList.setAdapter(adapter);

        // 3. CHỈ ADMIN MỚI CÓ SỰ KIỆN CLICK SỬA/XÓA
        if (isAdmin) {
            adapter.setOnVoucherClickListener(this::showEditVoucherDialog);
            fabAddVoucher.setOnClickListener(v -> showAddVoucherDialog());
        } else {
            // User thường click vào chỉ hiện thông báo
            adapter.setOnVoucherClickListener(voucher ->
                    Toast.makeText(this, "Voucher: " + voucher.getCode() + " đang hiệu lực", Toast.LENGTH_SHORT).show());
        }

        loadVouchers();
    }

    private void loadVouchers() {
        db.collection("Vouchers").get().addOnSuccessListener(snapshots -> {
            voucherList.clear();
            for (DocumentSnapshot doc : snapshots) {
                Voucher v = doc.toObject(Voucher.class);
                if (v != null) voucherList.add(v);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void showAddVoucherDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_product, null); // Tận dụng layout cũ của Tuấn
        builder.setView(view);

        EditText edtCode = view.findViewById(R.id.edtEditProductName);
        EditText edtDiscount = view.findViewById(R.id.edtEditProductPrice);
        EditText edtDesc = view.findViewById(R.id.edtEditProductDescription);

        // Ẩn các thành phần không cần thiết
        view.findViewById(R.id.edtEditProductImages).setVisibility(View.GONE);
        view.findViewById(R.id.imgEditPreview).setVisibility(View.GONE);
        view.findViewById(R.id.btnSelectImage).setVisibility(View.GONE);
        view.findViewById(R.id.spnEditCategory).setVisibility(View.GONE);

        builder.setTitle("Thêm Voucher Mới");
        builder.setPositiveButton("THÊM", (dialog, which) -> {
            String code = edtCode.getText().toString().trim();
            String discount = edtDiscount.getText().toString().trim();
            String desc = edtDesc.getText().toString().trim();

            if (!code.isEmpty() && !discount.isEmpty()) {
                Voucher newV = new Voucher(code, discount, desc);
                db.collection("Vouchers").add(newV).addOnSuccessListener(doc -> loadVouchers());
            }
        });
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

        builder.setTitle("Chỉnh sửa Voucher");
        builder.setPositiveButton("LƯU", (dialog, which) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", edtCode.getText().toString());
            map.put("discount", edtDiscount.getText().toString());
            map.put("description", edtDesc.getText().toString());

            db.collection("Vouchers").whereEqualTo("code", voucher.getCode()).get().addOnSuccessListener(q -> {
                if (!q.isEmpty()) {
                    db.collection("Vouchers").document(q.getDocuments().get(0).getId()).update(map)
                            .addOnSuccessListener(a -> loadVouchers());
                }
            });
        });

        builder.setNeutralButton("XÓA", (dialog, which) -> {
            db.collection("Vouchers").whereEqualTo("code", voucher.getCode()).get().addOnSuccessListener(q -> {
                if (!q.isEmpty()) {
                    db.collection("Vouchers").document(q.getDocuments().get(0).getId()).delete()
                            .addOnSuccessListener(a -> loadVouchers());
                }
            });
        });
        builder.show();
    }
}