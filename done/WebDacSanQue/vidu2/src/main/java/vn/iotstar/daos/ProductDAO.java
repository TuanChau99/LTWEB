package vn.iotstar.daos;

import java.util.List;
import vn.iotstar.entity.Product;

public interface ProductDAO {
    List<Product> getAll();
    Product getById(int id);
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    
    // Đưa 2 hàm stock cũ của bạn vào đây
    int getProductStock(int id);
    void updateStock(int productId, int quantityPurchased);
    List<Product> getLowStock();
    void restore(int id);
    List<Product> getDeletedProducts();
    Product findById(int id);
}