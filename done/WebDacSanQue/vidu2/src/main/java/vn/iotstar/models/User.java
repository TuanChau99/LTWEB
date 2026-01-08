package vn.iotstar.models;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class User implements Serializable {
    private int id;
    private String email;
    private String username; 
    private String fullname; 
    private String password;
    private String avatar;
    private int roleid;
    private String phone;
    private Date createdDate;
    private double totalSpent; // Dùng để nhận giá trị từ SUM(total_price)
    private String address;
    public User() {}

    // Constructor chuẩn hóa tên biến
    public User(int id, String email, String username, String fullname, String password, String avatar, int roleid,
            String phone, Date createdDate, double totalSpent) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.avatar = avatar;
        this.roleid = roleid;
        this.phone = phone;
        this.createdDate = createdDate;
        this.totalSpent = totalSpent;
    }

    // --- GETTER & SETTER CHUẨN (Quan trọng để JSP không báo lỗi) ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; } // Khớp với ${c.username}
    public void setUsername(String username) { this.username = username; }

    public String getFullname() { return fullname; } // Khớp với ${c.fullname}
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public int getRoleid() { return roleid; }
    public void setRoleid(int roleid) { this.roleid = roleid; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public double getTotalSpent() { return totalSpent; } // Sửa lỗi "not readable"
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}