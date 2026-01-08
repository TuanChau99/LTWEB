package vn.iotstar.models;

import java.io.Serializable;
import java.sql.Date;

public class OrderModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private double totalPrice; // Đổi từ amount sang totalPrice để khớp với SQL
    private Date orderDate;
    private int status;
    private String address;
    
    // Thêm đối tượng UserModel để chứa thông tin khách hàng (Tên, Email)
    private UserModel user; 

    public OrderModel() {
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}