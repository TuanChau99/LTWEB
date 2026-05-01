package com.example.tuantc;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PolicyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        TextView tvTitle = findViewById(R.id.tvPolicyTitle);
        TextView tvContent = findViewById(R.id.tvPolicyContent);
        ImageView btnBack = findViewById(R.id.btnBackPolicy);

        btnBack.setOnClickListener(v -> finish());

        String type = getIntent().getStringExtra("policy_type");

        if ("warranty".equals(type)) {
            tvTitle.setText("Chính Sách Bảo Hành & Đổi Trả");

            // Nội dung mới phù hợp cho Shop Thời Trang Nam
            String content = "Chào bạn, tại Tuantc Shop, chúng tôi cam kết mang lại trải nghiệm tốt nhất cho quý ông:\n\n" +
                    "1. CHÍNH SÁCH ĐỔI SIZE:\n" +
                    "   • Hỗ trợ đổi size trong vòng 7 ngày kể từ ngày nhận hàng.\n" +
                    "   • Sản phẩm đổi phải còn nguyên tem mác, chưa qua sử dụng hay giặt là.\n\n" +
                    "2. BẢO HÀNH CHẤT LƯỢNG (VIP PRO):\n" +
                    "   • Bảo hành đường kim mũi chỉ trong vòng 06 tháng.\n" +
                    "   • 1 đổi 1 ngay lập tức nếu vải bị ra màu hoặc xù lông nặng trong lần giặt đầu tiên.\n\n" +
                    "3. ĐỔI TRẢ MIỄN PHÍ:\n" +
                    "   • Miễn phí 100% chi phí đổi trả nếu shop gửi nhầm mẫu, nhầm size hoặc sản phẩm có lỗi từ nhà sản xuất.\n\n" +
                    "4. KHÁCH HÀNG THÂN THIẾT:\n" +
                    "   • Thành viên từ hạng Bạc trở lên được hỗ trợ đổi trả tại nhà (Shipper đến lấy hàng tận nơi).\n\n" +
                    "Cảm ơn bạn đã tin tưởng lựa chọn phong cách cùng chúng tôi!";

            tvContent.setText(content);
        }
    }
}