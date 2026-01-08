package vn.iotstar.entity;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    // Map lưu trữ: Product ID (Integer) -> Product (Product)
    private Map<Integer, Product> items;

    public Cart() {
        items = new HashMap<>();
    }
    
    // Phương thức kiểm tra sản phẩm đã tồn tại chưa
    private boolean isItemExist(int id) {
        return items.containsKey(id);
    }
    
    // Phương thức thêm/cập nhật sản phẩm
    public void addProduct(Product newProduct) {
        if (isItemExist(newProduct.getId())) {
            // Nếu sản phẩm đã tồn tại, tăng số lượng lên 1
            Product existingProduct = items.get(newProduct.getId());
            existingProduct.setQuantity(existingProduct.getQuantity() + 1);
        } else {
            // Nếu chưa có, đặt số lượng là 1
            newProduct.setQuantity(1);
            items.put(newProduct.getId(), newProduct);
        }
    }
    
    // Phương thức xóa sản phẩm
    public void removeItem(int id) {
        items.remove(id);
    }

    // Phương thức cập nhật số lượng
    public void updateQuantity(int id, int quantity) {
        if (items.containsKey(id)) {
            items.get(id).setQuantity(quantity);
            if (quantity <= 0) {
                removeItem(id); // Xóa nếu số lượng <= 0
            }
        }
    }
    
    // Tính tổng tiền (Ví dụ: Giả sử Product có getPrice() và getQuantity())
    public double getTotalPrice() {
        double total = 0;
        for (Product product : items.values()) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    // Getter và Setter
    public Map<Integer, Product> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Product> items) {
        this.items = items;
    }

	public double getTotalMoney() {
		double t = 0;
	    // Duyệt qua danh sách các Product có trong Map items
	    for (Product p : items.values()) {
	        t += (p.getQuantity() * p.getPrice());
	    }
	    return t;
	}
}