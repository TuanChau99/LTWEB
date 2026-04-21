package com.example.tuantc;

import java.io.Serializable;

public class Voucher implements Serializable {
    private String code;        // Mã voucher (vd: VIMEN10)
    private String discount;    // Số tiền hoặc % giảm (vd: 10% hoặc 20k)
    private String description; // Mô tả (vd: Giảm cho đơn từ 100k)
    private String expiryDate;  // Ngày hết hạn

    public Voucher() {}

    public Voucher(String code, String discount, String description) {
        this.code = code;
        this.discount = discount;
        this.description = description;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDiscount() { return discount; }
    public void setDiscount(String discount) { this.discount = discount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}