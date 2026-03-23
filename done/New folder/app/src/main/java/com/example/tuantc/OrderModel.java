package com.example.tuantc;

import java.util.List;

public class OrderModel {
    private String orderId;
    private String customerName;
    private String customerPhone;
    private String shippingAddress;
    private String totalPrice;
    private String status;      // Đang giao, Hoàn thành, Đã hủy
    private String orderDate;     // Thời gian đặt
    private List<CartItem> items; // Danh sách sản phẩm trong đơn

    public OrderModel() {}

    public OrderModel(String orderId, String customerName, String customerPhone, String shippingAddress, String totalPrice, String status, String orderDate, List<CartItem> items) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.shippingAddress = shippingAddress;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
        this.items = items;
    }

    // Getter và Setter
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}