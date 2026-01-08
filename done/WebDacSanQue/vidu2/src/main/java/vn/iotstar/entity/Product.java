package vn.iotstar.entity; 

public class Product {
     
    private int id;
    private String name;
    private String image;
    private double price;
    private String title;
    private String description;
    
    // ⭐ ĐÃ SỬA: cate_id thành cateID để khớp với lệnh product.setCateID() trong DAO
    private int cateID; 
    private int sell_id; 
    
    private int quantity; 
    private int stock;
    private int status;
    
    public Product() {
    }

    // Cập nhật Constructor đầy đủ tham số
    public Product(int id, String name, String image, double price, String title, String description, int cateID, int sell_id) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.title = title;
        this.description = description;
        this.cateID = cateID; //
        this.sell_id = sell_id;
    }
    
    public Product(int id, String name, String image, double price, String title, String description) {
        this(id, name, image, price, title, description, 0, 0); 
    }
    
    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    // ⭐ ĐÃ SỬA: Getter/Setter mới khớp chính xác với tên gọi trong ProductDaoImpl
    public int getCateID() { return cateID; }
    public void setCateID(int cateID) { this.cateID = cateID; }

    public int getSell_id() { return sell_id; } 
    public void setSell_id(int sell_id) { this.sell_id = sell_id; } 
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", cateID=" + cateID + 
                ", sell_id=" + sell_id + 
                ", quantity=" + quantity + 
                ", status=" + status + 
                '}';
    }
}