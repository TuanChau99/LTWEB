package vn.iotstar.entity;

public class OrderDetail {
    private int id;
    private int orderId;
    private int productId;
    private String productName;  // Lấy từ bảng Product để hiển thị tên món
    private String productImage; // Lấy từ bảng Product để hiển thị ảnh món
    private int quantity;
    private double price;

    public OrderDetail() {
    }

    public OrderDetail(int id, int orderId, int productId, String productName, String productImage, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "id=" + id + ", orderId=" + orderId + ", productName=" + productName + ", quantity=" + quantity + ", price=" + price + '}';
    }
}