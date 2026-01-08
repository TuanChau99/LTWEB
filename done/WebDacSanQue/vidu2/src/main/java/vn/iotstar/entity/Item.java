package vn.iotstar.entity;

public class Item {
    private Product product; // Đối tượng sản phẩm
    private int quantity;    // Số lượng mua
    private double price;    // Giá tại thời điểm mua

    public Item() {}

    public Item(Product product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters và Setters
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}