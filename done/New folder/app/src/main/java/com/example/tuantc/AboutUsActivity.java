package com.example.tuantc;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class AboutUsActivity extends AppCompatActivity {

    private static final int CONTACT_PERMISSION_CODE = 100;
    ImageView btnBack, imgStaticMap;
    LinearLayout layoutCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack);
        imgStaticMap = findViewById(R.id.imgStaticMap);
        layoutCall = findViewById(R.id.layoutCall); // LinearLayout chứa SĐT

        // 1. Nút Back
        btnBack.setOnClickListener(v -> finish());

        // 2. Mở Google Maps khi click vào ảnh bản đồ
        imgStaticMap.setOnClickListener(v -> {
            // Tọa độ HCMUTE: 10.8507, 106.7721
            // Tham số q=... giúp hiển thị nhãn tên cửa hàng trên bản đồ
            Uri gmmIntentUri = Uri.parse("geo:10.8507,106.7721?q=10.8507,106.7721(Vimen+Store+HCMUTE)");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Nếu máy không có Google Maps, mở bằng trình duyệt
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=10.8507,106.7721"));
                startActivity(webIntent);
            }
        });

        // 3. Nhấn giữ để lưu danh bạ (Sử dụng Content Provider)
        layoutCall.setOnLongClickListener(v -> {
            checkPermissionAndSaveContact();
            return true;
        });
    }

    // Kiểm tra quyền WRITE_CONTACTS trước khi thao tác
    private void checkPermissionAndSaveContact() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            saveContactToSystem();
        } else {
            // Xin quyền nếu chưa có
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, CONTACT_PERMISSION_CODE);
        }
    }

    // Hàm thực hiện lưu danh bạ qua ContentProvider
    private void saveContactToSystem() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Thêm RawContact
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Thêm Tên hiển thị
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "Vimen Store Support")
                .build());

        // Thêm Số điện thoại
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "0777550500")
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());

        try {
            // Sử dụng ContentResolver để thực thi batch
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Đã lưu Vimen Store vào danh bạ!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi lưu danh bạ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý kết quả sau khi người dùng nhấn "Cho phép" hoặc "Từ chối" quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveContactToSystem();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để lưu danh bạ!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}