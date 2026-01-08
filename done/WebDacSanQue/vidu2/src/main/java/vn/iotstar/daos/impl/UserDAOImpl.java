package vn.iotstar.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.configs.DBConnect;
import vn.iotstar.daos.UserDAO;
import vn.iotstar.models.User;
import vn.iotstar.models.UserModel;

public class UserDAOImpl implements UserDAO {
	
	@Override
	public List<UserModel> getAllCustomers() {
	    List<UserModel> list = new ArrayList<>();
	    // 1. THÊM u.address VÀO SELECT VÀ GROUP BY
	    String sql = "SELECT u.id, u.email, u.username, u.fullname, u.password, u.avatar, u.roleid, u.phone, u.address, u.createdDate, "
	               + "ISNULL(SUM(o.total_price), 0) AS totalSpent "
	               + "FROM [User] u "
	               + "LEFT JOIN dbo.orders o ON u.id = o.user_id AND o.status = 2 "
	               + "WHERE u.roleid = 5 AND u.isDeleted = 0 "
	               + "GROUP BY u.id, u.email, u.username, u.fullname, u.password, u.avatar, u.roleid, u.phone, u.address, u.createdDate";
	    try (Connection conn = new DBConnect().getConnection(); 
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        
	        while (rs.next()) {
	        	UserModel user = new UserModel();
	            user.setId(rs.getInt("id"));
	            user.setEmail(rs.getString("email"));
	            user.setUsername(rs.getString("username"));
	            user.setFullname(rs.getString("fullname"));
	            user.setAvatar(rs.getString("avatar"));
	            user.setPhone(rs.getString("phone"));
	            // 2. GÁN ĐỊA CHỈ VÀO MODEL
	            user.setAddress(rs.getString("address")); 
	            user.setTotalSpent(rs.getDouble("totalSpent")); 
	            list.add(user);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	@Override
    public int countNewCustomers() {
        // Đảm bảo roleid = 5 và tên bảng [User] chính xác như bạn đã test thành công
        String query = "SELECT COUNT(*) FROM [User] WHERE roleid = 5 AND createdDate >= DATEADD(day, -30, GETDATE()) AND isDeleted = 0";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return 0;
    }
	
    @Override
    public List<UserModel> findAll() {
        List<UserModel> list = new ArrayList<>();
        String sql = "SELECT * FROM [User] WHERE isDeleted = 0 ORDER BY id ASC"; 
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
                user.setPassword(rs.getString("password"));
                user.setAvatar(rs.getString("avatar"));
                user.setRoleid(rs.getInt("roleid"));
                user.setPhone(rs.getString("phone"));
                // 3. THÊM ĐỊA CHỈ TRONG FINDALL
                user.setAddress(rs.getString("address")); 
                user.setCreateddate(rs.getDate("createdDate"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insert(UserModel user) {
        // 4. THÊM CỘT address VÀO CÂU LỆNH INSERT
        String sql = "INSERT INTO [User](email, username, fullname, password, avatar, roleid, phone, address, createdDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getAvatar());
            ps.setInt(6, user.getRoleid());
            ps.setString(7, user.getPhone());
            // Gán địa chỉ mặc định nếu user để trống
            ps.setString(8, user.getAddress() != null ? user.getAddress() : "Chưa xác định");
            ps.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public UserModel findById(int id) {
        String sql = "SELECT * FROM [User] WHERE id = ?"; 
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserModel user = new UserModel();
                    user.setId(rs.getInt("id")); 
                    user.setUsername(rs.getString("username"));
                    user.setFullname(rs.getString("fullname"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    // 5. THÊM ĐỊA CHỈ TRONG FINDBYID
                    user.setAddress(rs.getString("address")); 
                    user.setRoleid(rs.getInt("roleid"));
                    user.setAvatar(rs.getString("avatar"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void update(UserModel user) {
        // 6. CẬP NHẬT CỘT address TRONG LỆNH UPDATE
        String sql = "UPDATE [User] SET email=?, fullname=?, phone=?, address=?, roleid=?, avatar=? WHERE id=?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullname());
            ps.setString(3, user.getPhone());
            ps.setString(4, (user.getAddress() != null) ? user.getAddress() : "Chưa xác định");
            ps.setInt(5, user.getRoleid());
            ps.setString(6, user.getAvatar());
            ps.setInt(7, user.getId()); 
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserModel findByUserName(String username) {
        String sql = "SELECT * FROM [User] WHERE username = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserModel user = new UserModel();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setUsername(rs.getString("username"));
                    user.setFullname(rs.getString("fullname"));
                    user.setPassword(rs.getString("password"));
                    user.setAvatar(rs.getString("avatar"));
                    user.setRoleid(rs.getInt("roleid"));
                    user.setPhone(rs.getString("phone"));
                    // THÊM ĐỊA CHỈ TRONG FINDBYUSERNAME
                    user.setAddress(rs.getString("address")); 
                    user.setCreateddate(rs.getDate("createdDate"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
 // 1. Lấy danh sách người dùng đã bị xóa (isDeleted = 'True')
    @Override
    public List<UserModel> getDeletedUsers() {
        List<UserModel> list = new ArrayList<>();
        // Truy vấn những User có isDeleted là True
        String sql = "SELECT * FROM [User] WHERE isDeleted = 1";
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                // Set các trường khác nếu cần hiển thị thêm
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Khôi phục người dùng (Chuyển isDeleted về 'False')
    @Override
    public void restoreUser(int id) {
        // Cập nhật lại trạng thái isDeleted thành False để người dùng xuất hiện lại
        String sql = "UPDATE [User] SET isDeleted = 0 WHERE id = ?";
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(int id) {
    	softDelete(id);
    }
    
    @Override
    public void softDelete(int id) {
        String sql = "UPDATE [User] SET isDeleted = 1 WHERE id = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean checkExistEmail(String email) {
        String sql = "SELECT 1 FROM [User] WHERE email = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean checkExistUsername(String username) {
        String sql = "SELECT 1 FROM [User] WHERE username = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    @Override
    public void updateProfile(int id, String fullname, String email, String phone, String address) {
        // Lưu ý: Tên bảng là [User] theo các phương thức trên của bạn
        String sql = "UPDATE [User] SET fullname = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, (address != null) ? address : "Chưa xác định");
            ps.setInt(5, id);
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean checkExistPhone(String phone) {
        String sql = "SELECT 1 FROM [User] WHERE phone = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

	@Override
	public void insert(User user) {
		// Dùng bản UserModel ở trên
	}

}