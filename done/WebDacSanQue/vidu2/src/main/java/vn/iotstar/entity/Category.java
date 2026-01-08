package vn.iotstar.entity;

public class Category {
    // ⭐ ĐÃ SỬA: Đổi tên biến thành cate_id để khớp hoàn toàn với CSDL
    private int cate_id;
    private String cate_name;
    private String icons;

    public Category() {
    }

    public Category(int cate_id, String cate_name, String icons) {
        this.cate_id = cate_id;
        this.cate_name = cate_name;
        this.icons = icons;
    }

    // ⭐ ĐÃ SỬA: Getter/Setter đồng nhất với cate_id
    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    @Override
    public String toString() {
        return "Category{" +
                "cate_id=" + cate_id +
                ", cate_name='" + cate_name + '\'' +
                ", icons='" + icons + '\'' +
                '}';
    }
}