package com.example.tuantc;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderHistoryActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ImageView btnBackOrder;
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Lấy quyền Admin được gửi từ ProfileActivity
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        tabLayout = findViewById(R.id.tabLayoutOrder);
        viewPager = findViewById(R.id.viewPagerOrder);
        btnBackOrder = findViewById(R.id.btnBackOrder);

        if (btnBackOrder != null) {
            btnBackOrder.setOnClickListener(v -> finish());
        }

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                String status;
                switch (position) {
                    case 1: status = "Đang giao"; break;
                    case 2: status = "Hoàn thành"; break;
                    case 3: status = "Đã hủy"; break;
                    default: status = "Tất cả"; break;
                }
                // TRUYỀNisAdmin VÀO FRAGMENT
                return new OrderListFragment(status, isAdmin);
            }

            @Override
            public int getItemCount() { return 4; }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Tất cả"); break;
                case 1: tab.setText("Đang giao"); break;
                case 2: tab.setText("Hoàn thành"); break;
                case 3: tab.setText("Đã hủy"); break;
            }
        }).attach();

        int selectedTab = getIntent().getIntExtra("selected_tab", 0);
        viewPager.setCurrentItem(selectedTab, false);
    }
}