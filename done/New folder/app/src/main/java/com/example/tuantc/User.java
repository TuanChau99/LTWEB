package com.example.tuantc;

import java.io.Serializable;

public class User implements Serializable {
    private String id;       // Dùng để lưu Document ID từ Firestore
    private String username;
    private String password;
    private String rules;    // "admin" hoặc "user"

    // Constructor mặc định (bắt buộc phải có để Firebase Firestore hoạt động)
    public User() {
    }

    // Constructor đầy đủ
    public User(String username, String password, String rules) {
        this.username = username;
        this.password = password;
        this.rules = rules;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }
}