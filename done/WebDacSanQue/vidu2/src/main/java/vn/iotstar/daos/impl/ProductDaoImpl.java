package vn.iotstar.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.configs.DBConnect;
import vn.iotstar.daos.ProductDAO;
import vn.iotstar.entity.Product;

public class ProductDaoImpl implements ProductDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    @Override
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        // Lấy tất cả sản phẩm, sắp xếp ID giảm dần để món mới nhất hiện lên đầu
        String query = "SELECT * FROM [product] WHERE [status] = 1 ORDER BY id DESC";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setImage(rs.getString("image"));
                p.setPrice(rs.getDouble("price"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setCateID(rs.getInt("cateID"));
                p.setSell_id(rs.getInt("sell_ID"));
                p.setStock(rs.getInt("stock")); // Cột này dùng để hiển thị tồn kho trên Dashboard
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }
    
    @Override
    public List<Product> getLowStock() {
        List<Product> list = new ArrayList<>();
        // Lấy những sản phẩm có stock dưới 10
        String query = "SELECT * FROM product WHERE [status] = 1 and stock < 10 ORDER BY stock ASC";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setStock(rs.getInt("stock"));
                p.setPrice(rs.getDouble("price"));
                list.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    @Override
    public Product getById(int id) {
        String query = "SELECT * FROM product WHERE [status] = 1 and id = ?";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setImage(rs.getString("image"));
                p.setPrice(rs.getDouble("price"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setCateID(rs.getInt("cateID"));
                p.setSell_id(rs.getInt("sell_ID"));
                p.setStock(rs.getInt("stock"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    @Override
    public void insert(Product p) {
    	String query = "INSERT INTO product (name, [image], price, title, [description], cateID, sell_ID, stock) VALUES (?,?,?,?,?,?,?,?)";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, p.getName());
            ps.setString(2, p.getImage());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getTitle());
            ps.setString(5, p.getDescription());
            ps.setInt(6, p.getCateID());
            ps.setInt(7, p.getSell_id());
            ps.setInt(8, p.getStock());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    
 // 1. Lấy danh sách sản phẩm đã bị xóa mềm
    @Override
    public List<Product> getDeletedProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM [product] WHERE [status] = 0"; // Lọc status = 0
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                // ... set các trường khác tương tự hàm getAll ...
                list.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Hàm khôi phục sản phẩm
    @Override
    public void restore(int id) {
        String sql = "UPDATE [product] SET [status] = 1 WHERE [id] = ?";
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @Override
    public void update(Product p) {
        String query = "UPDATE product SET [name]=?, [image]=?, [price]=?, [title]=?, [description]=?, [cateID]=?, [stock]=? WHERE [id]=?";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, p.getName());
            ps.setString(2, p.getImage());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getTitle());
            ps.setString(5, p.getDescription());
            ps.setInt(6, p.getCateID());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    @Override
    public void delete(int id) {
        // Thay vì DELETE FROM product WHERE id = ?
        String sql = "UPDATE [product] SET [status] = 0 WHERE [id] = ?";
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Đã xóa mềm sản phẩm ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- CÁC HÀM QUẢN LÝ KHO (STOCK) CŨ CỦA BẠN ---
    @Override
    public int getProductStock(int id) {
        String query = "SELECT stock FROM product WHERE id = ?";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return 0;
    }

    @Override
    public void updateStock(int productId, int quantityPurchased) {
        String sql = "UPDATE product SET stock = stock - ? WHERE id = ?";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantityPurchased);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    @Override
    public Product findById(int id) {
        // 1. Khai báo câu lệnh SQL dựa trên bảng thực tế
        String sql = "SELECT * FROM product WHERE id = ?";
        
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 2. Tạo đối tượng và ánh xạ từng cột từ DB vào Model
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setImage(rs.getString("image"));
                    product.setPrice(rs.getDouble("price"));
                    product.setTitle(rs.getString("title"));
                    product.setDescription(rs.getString("description"));
                    product.setCateID(rs.getInt("cateID"));
                    product.setStock(rs.getInt("stock"));
                    product.setStatus(rs.getInt("status"));
                    
                    return product;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // Hàm phụ để đóng kết nối an toàn
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}