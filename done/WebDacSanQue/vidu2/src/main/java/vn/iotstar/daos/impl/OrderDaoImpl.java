package vn.iotstar.daos.impl;

import vn.iotstar.configs.DBConnect;
import vn.iotstar.entity.Cart;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderDetail;
import vn.iotstar.models.OrderModel;
import vn.iotstar.models.UserModel;
import vn.iotstar.daos.OrderDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.iotstar.entity.Product;

public class OrderDaoImpl implements OrderDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    @Override
    // 1. Lấy tất cả đơn hàng cho Admin
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String query = "SELECT o.*, u.username, u.phone, ISNULL(o.address, N'Chưa cung cấp địa chỉ') as shipping_address " +
                "FROM [orders] o JOIN [user] u ON o.user_id = u.id ORDER BY o.order_date ASC";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserId(rs.getInt("user_id"));
                o.setUserName(rs.getString("username"));
                o.setPhone(rs.getString("phone"));
                o.setAddress(rs.getString("shipping_address"));
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setStatus(rs.getInt("status"));
                list.add(o);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    @Override
    public void updateStatus(int orderId, int status) {
        String queryUpdateOrder = "UPDATE [orders] SET [status] = ? WHERE id = ?";
        // Sử dụng biến cục bộ hoàn toàn để tránh xung đột với biến Global của Class
        Connection connection = null; 
        try {
            connection = new DBConnect().getConnection();
            connection.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Cập nhật trạng thái đơn hàng chính
            try (PreparedStatement psUpdate = connection.prepareStatement(queryUpdateOrder)) {
                psUpdate.setInt(1, status);
                psUpdate.setInt(2, orderId);
                psUpdate.executeUpdate();
            }

            // 2. Xử lý kho hàng (Hủy=3) hoặc Doanh số (Hoàn thành=2)
            if (status == 3 || status == 2) { 
                String queryGetDetails = "SELECT product_id, quantity FROM [order_details] WHERE order_id = ?";
                String queryUpdateProduct = (status == 3) 
                    ? "UPDATE [product] SET [stock] = [stock] + ? WHERE [id] = ?" 
                    : "UPDATE [product] SET [sold] = ISNULL([sold], 0) + ? WHERE [id] = ?";
                
                try (PreparedStatement psDetail = connection.prepareStatement(queryGetDetails);
                     PreparedStatement psProduct = connection.prepareStatement(queryUpdateProduct)) {
                    
                    psDetail.setInt(1, orderId);
                    try (ResultSet rsDetail = psDetail.executeQuery()) {
                        while(rsDetail.next()) {
                            psProduct.setInt(1, rsDetail.getInt("quantity"));
                            psProduct.setInt(2, rsDetail.getInt("product_id"));
                            psProduct.addBatch();
                        }
                        psProduct.executeBatch();
                    }
                }
            }
            
            connection.commit(); // Lệnh này cực kỳ quan trọng để lưu vào DB
            System.out.println("✅ DB Updated: Order " + orderId + " set to status " + status);
            
        } catch (Exception e) {
            if(connection != null) {
                try { 
                    connection.rollback(); 
                    System.err.println("❌ Rollback executed due to error.");
                } catch(Exception ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            try { if(connection != null) connection.close(); } catch(Exception ex) { ex.printStackTrace(); }
        }
    }
    
    @Override
    // 3. Xem chi tiết một đơn hàng
    public List<OrderDetail> getOrderDetailByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        // JOIN với bảng product để lấy tên (name) và ảnh (image)
        String query = "SELECT d.*, p.[name], p.[image] FROM [order_details] d "
                     + "JOIN [product] p ON d.product_id = p.id WHERE d.order_id = ?";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderDetail d = new OrderDetail();
                d.setId(rs.getInt("id"));
                d.setOrderId(rs.getInt("order_id"));
                d.setProductId(rs.getInt("product_id"));
                d.setProductName(rs.getString("name"));   // Lấy cột name từ bảng product
                d.setProductImage(rs.getString("image")); // Lấy cột image từ bảng product
                d.setQuantity(rs.getInt("quantity"));
                d.setPrice(rs.getDouble("price"));
                list.add(d);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    @Override
 // 4. Lấy danh sách đơn hàng của một khách hàng cụ thể (Lịch sử mua hàng)
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        // Truy vấn lấy các đơn hàng của User đó, sắp xếp đơn mới nhất lên đầu
        String query = "SELECT * FROM [orders] WHERE user_id = ? ORDER BY order_date DESC";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserId(rs.getInt("user_id"));
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setStatus(rs.getInt("status"));
                list.add(o);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally {
            // Đóng kết nối an toàn
            try { if(conn != null) conn.close(); } catch(Exception e) {}
        }
        return list;
    }
    
    @Override
    public int countPendingOrders() {
        String query = "SELECT COUNT(*) FROM [orders] WHERE [status] = 0";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
    
    @Override
    public List<OrderModel> findByUserId(int userId) {
        List<OrderModel> list = new ArrayList<>();
        // Sử dụng tên bảng dbo.orders theo đúng ảnh database của bạn
        String sql = "SELECT id, user_id, total_price, order_date, status, address "
                   + "FROM dbo.orders WHERE user_id = ? "
                   + "ORDER BY order_date DESC"; // Lấy đơn hàng mới nhất lên đầu
        
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderModel order = new OrderModel();
                    // Ánh xạ chính xác các cột từ ảnh image_f3f4e1.png
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    if (rs.getTimestamp("order_date") != null) {
                    	order.setOrderDate(new java.sql.Date(rs.getTimestamp("order_date").getTime()));
                    }
                    
                    order.setStatus(rs.getInt("status"));
                    
                    // Sửa lỗi dòng 210: Kiểm tra kỹ tên phương thức trong OrderModel
                    order.setAddress(rs.getString("address"));
                  
                    
                    list.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public Order getOrderById(int orderId) {
        // JOIN với bảng user để lấy thông tin người đặt hàng
        String query = "SELECT o.*, u.username, u.phone, ISNULL(o.address, u.address) as shipping_address " +
                       "FROM [orders] o JOIN [user] u ON o.user_id = u.id WHERE o.id = ?";
        try {
            // Mở kết nối mới để lấy dữ liệu
            Connection conn = new DBConnect().getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserId(rs.getInt("user_id"));
                o.setUserName(rs.getString("username"));
                o.setPhone(rs.getString("phone"));
                // Ưu tiên lấy địa chỉ ghi chú trong đơn, nếu ko có thì lấy địa chỉ mặc định của User
                o.setAddress(rs.getString("shipping_address")); 
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setStatus(rs.getInt("status"));
                return o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public double getTotalRevenueThisMonth() {
        double total = 0;
        // Câu lệnh SQL JOIN 3 bảng để tính doanh thu thực tế
        String sql = "SELECT SUM(p.price * od.quantity) FROM orders o " +
                     "JOIN order_details od ON o.id = od.order_id " +
                     "JOIN product p ON od.product_id = p.id " +
                     "WHERE MONTH(o.order_date) = MONTH(GETDATE()) " +
                     "AND YEAR(o.order_date) = YEAR(GETDATE())";
        try {
            conn = new DBConnect().getConnection(); // Đảm bảo kết nối DB
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    
    @Override
    public List<OrderModel> getRecentOrders(int limit, String keyword) {
        List<OrderModel> list = new ArrayList<>();
     // Loại bỏ dấu # nếu người dùng nhập vào (ví dụ #20 thành 20)
        String cleanKeyword = (keyword != null) ? keyword.replace("#", "") : "";
        
        // SQL: Thêm điều kiện tìm theo ID (đã convert sang chuỗi)
        String sql = "SELECT TOP " + limit + " o.*, u.fullname, u.email " +
                     "FROM orders o JOIN [User] u ON o.user_id = u.id " +
                     "WHERE u.fullname LIKE ? OR u.email LIKE ? OR CAST(o.id AS VARCHAR) LIKE ? " + 
                     "ORDER BY o.order_date DESC";
        try {
            conn = new DBConnect().getConnection();
            ps = conn.prepareStatement(sql);
            String searchKey = "%" + cleanKeyword + "%";
            
            ps.setString(1, searchKey); // Tìm theo tên
            ps.setString(2, searchKey); // Tìm theo email
            ps.setString(3, searchKey); // Tìm theo ID
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderModel order = new OrderModel();
                order.setId(rs.getInt("id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setStatus(rs.getInt("status"));
                
                UserModel user = new UserModel();
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                order.setUser(user);
                list.add(order);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    @Override
    public int insertOrder(UserModel u, Cart cart, String address) { // Thêm String address
        LocalDate curDate = LocalDate.now();
        String date = curDate.toString();
        int generatedId = -1;
        
        try {
            conn = new DBConnect().getConnection();
            // Cập nhật câu lệnh SQL thêm cột [address]
            String query = "INSERT INTO [orders] (user_id, total_price, order_date, [status], [address]) VALUES (?, ?, ?, 0, ?)";
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, u.getId());
            ps.setDouble(2, cart.getTotalMoney());
            ps.setString(3, date);
            ps.setString(4, address); // Gán địa chỉ nhận hàng vào đây
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
                
                for (Product p : cart.getItems().values()) {
                    // Chèn order_details
                    String query2 = "INSERT INTO [order_details] (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps2 = conn.prepareStatement(query2);
                    ps2.setInt(1, generatedId);
                    ps2.setInt(2, p.getId());
                    ps2.setInt(3, p.getQuantity());
                    ps2.setDouble(4, p.getPrice());
                    ps2.executeUpdate();

                    // Cập nhật tồn kho
                    String queryUpdateStock = "UPDATE [product] SET [stock] = [stock] - ? WHERE [id] = ?";
                    PreparedStatement psStock = conn.prepareStatement(queryUpdateStock);
                    psStock.setInt(1, p.getQuantity());
                    psStock.setInt(2, p.getId());
                    psStock.executeUpdate();
                    
                    ps2.close();
                    psStock.close();
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return generatedId;
    }
    
    @Override
    public void deleteOrder(int orderId) {
        // Lưu ý: Phải xóa order_details trước do ràng buộc khóa ngoại (Foreign Key)
        String sqlDetail = "DELETE FROM order_details WHERE order_id = ?";
        String sqlOrder = "DELETE FROM orders WHERE id = ?";
        try {
        	conn = new DBConnect().getConnection(); 
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setInt(1, orderId);
            psDetail.executeUpdate();

            PreparedStatement psOrder = conn.prepareStatement(sqlOrder);
            psOrder.setInt(1, orderId);
            psOrder.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public Map<String, Double> getRevenueLast7Days() {
		// TODO Auto-generated method stub
		return null;
	}

	
}