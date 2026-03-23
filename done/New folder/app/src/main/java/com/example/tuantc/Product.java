package com.example.tuantc;

import java.util.ArrayList;
import java.util.List;
public class Product {
    private String id; // ID tự động từ Firestore
    private String name;
    private String price;
    private List<String> images;
    private String rating;
    private String category;
    private int quantity = 1;
    private String orderId;
    private String orderDate;
    private String size;
    private String status;
    private String oldPrice;
    //  Constructor mặc định để Firestore có thể map dữ liệu
    public Product() {
        this.images = new ArrayList<>();
    }

    public Product(String id, String name, String price, List<String> images, String rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.images = images;
        this.rating = rating;
    }

    // Các hàm Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOldPrice() { return oldPrice; }
    public void setOldPrice(String oldPrice) { this.oldPrice = oldPrice; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    // Thêm hàm tiện ích để lấy ảnh đầu tiên làm ảnh đại diện
    public String getPrimaryImage() {
        if (images != null && !images.isEmpty()) {
            return images.get(0);
        }
        return "ic_launcher_background"; // Ảnh mặc định nếu lỗi
    }
}