package vn.iotstar.models;

public class Category {
    private int id; // hoặc cateID để khớp DB
    private String name; // hoặc cate_name để khớp DB
    private String icon;

    public Category() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; } // Đảm bảo hàm này tồn tại

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}