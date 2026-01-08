package vn.iotstar.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.configs.DBConnect; 
import vn.iotstar.entity.Product; 
import vn.iotstar.entity.Category;

public class DAO {

    private static final int RELATED_PRODUCT_LIMIT = 4; 

    // ⭐ SỬA LỖI TÊN CỘT: Dùng [cateID] và [sell_ID] theo cấu trúc DB của bạn
    private static final String ALL_COLUMNS = "[id], [name], [image], [price], [title], [description], [cateID], [sell_ID]";

    /**
     * Helper method để tạo đối tượng Product từ ResultSet
     */
    private Product mapResultSetToProduct(ResultSet rs) throws Exception {
        return new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("image"),
            rs.getDouble("price"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("cateID"), // ⭐ Dùng cateID
            rs.getInt("sell_ID") // ⭐ Dùng sell_ID
        );
    }
    
    /**
     * Phương thức lấy tất cả sản phẩm
     */
    public List<Product> getAllProduct() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT " + ALL_COLUMNS + " FROM dbo.product"; 

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return list;
    }
    
    /**
     * Phương thức lấy tất cả Category (Giữ nguyên)
     */
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM dbo.Category"; 

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("cateID"), 
                        rs.getString("cate_name"),
                        rs.getString("icons")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return list;
    }
    
    /**
     * Phương thức lấy Sản phẩm cuối cùng (Cập nhật chuỗi cột)
     */
    public Product getLastProduct() {
        Product product = null;
        String query = "SELECT TOP 1 " + ALL_COLUMNS + " FROM dbo.product ORDER BY id DESC";
        
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                product = mapResultSetToProduct(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return product;
    }
    
    /**
     * Phương thức lấy thông tin chi tiết của một sản phẩm dựa trên ID (Cập nhật chuỗi cột)
     */
    public Product getProductByID(int id) {
        Product product = null;
        String query = "SELECT " + ALL_COLUMNS + " FROM dbo.product WHERE id = ?"; 

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = mapResultSetToProduct(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Phương thức lấy HÀNG CÙNG LOẠI (Related Products)
     */
    public List<Product> getRelatedProductsByCID(int cid, int currentPid) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT TOP " + RELATED_PRODUCT_LIMIT + " " + ALL_COLUMNS + 
                       " FROM dbo.product WHERE cateID = ? AND id != ? ORDER BY id DESC";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, cid);
            ps.setInt(2, currentPid);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return list;
    }
    
    /**
     * Phương thức lấy HÀNG CÙNG NHÀ CUNG CẤP (Supplier Products)
     */
    public List<Product> getProductsBySupplier(int sid, int currentPid) {
        List<Product> list = new ArrayList<>();
        // ⭐ SỬA LỖI TÊN CỘT: Dùng sell_ID
        String query = "SELECT TOP " + RELATED_PRODUCT_LIMIT + " " + ALL_COLUMNS + 
                       " FROM dbo.product WHERE sell_ID = ? AND id != ? ORDER BY id DESC";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, sid);
            ps.setInt(2, currentPid);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return list;
    }
    
    /**
     * Phương thức lấy danh sách sản phẩm theo Category ID
     */
    public List<Product> getProductByCID(int cid) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT " + ALL_COLUMNS + " FROM dbo.product WHERE cateID = ?";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, cid); 
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return list;
    }
    
    /**
     * Phương thức LẤY SẢN PHẨM THEO TỪ KHÓA
     */
     public List<Product> searchByName(String txtSearch) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT " + ALL_COLUMNS + " FROM dbo.product WHERE [name] LIKE ?"; 

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, "%" + txtSearch + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return list;
    }
}