package com.example.tuantc;

public class CartItem {
    private String productId;
    private String name;
    private String price;
    private String image;
    private String size;
    private int quantity;

    public CartItem() {}

    public CartItem(String productId, String name, String price, String image, String size, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.size = size;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}