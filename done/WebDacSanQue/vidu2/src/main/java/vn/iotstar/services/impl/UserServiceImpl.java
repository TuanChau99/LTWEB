package vn.iotstar.services.impl;

import java.util.List;
import vn.iotstar.daos.UserDAO;
import vn.iotstar.daos.impl.UserDAOImpl;
import vn.iotstar.models.User;
import vn.iotstar.models.UserModel;
import vn.iotstar.services.UserService;

public class UserServiceImpl implements UserService {

    UserDAO userDao = new UserDAOImpl();

    // 1. Dùng cho trang Admin - Thêm mới User
    @Override
    public void insert(UserModel user) {
        // ⭐ QUAN TRỌNG: Phải gọi xuống DAO thì dữ liệu mới lưu được
        userDao.insert(user); 
    }

    // 2. Dùng cho trang Register - Khách hàng đăng ký
    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        
        // Chuyển đổi thông tin đăng ký thành UserModel để dùng chung hàm insert
        UserModel newUser = new UserModel();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setFullname(fullname);
        newUser.setPhone(phone);
        newUser.setRoleid(5); // Mặc định role User
        newUser.setAvatar("null");
        // Ngày tạo sẽ được UserDaoImpl tự động xử lý bằng System.currentTimeMillis()
        
        userDao.insert(newUser);
        return true;
    }

    @Override
    public List<UserModel> findAll() {
        return userDao.findAll();
    }

    @Override
    public UserModel login(String username, String password) {
        UserModel user = this.findByUserName(username);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public UserModel findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    // Các hàm kiểm tra tồn tại
    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.checkExistPhone(phone);
    }

    // --- Các hàm tạm thời chưa dùng hoặc cần bổ sung sau ---
    @Override
    public void update(UserModel user) {
        // Bổ sung sau khi viết update trong UserDaoImpl
    	userDao.update(user);
    }

    @Override
    public UserModel findById(int id) {
    	// Gọi xuống DAO để lấy dữ liệu thực tế thay vì return null
        return userDao.findById(id);
//    	return null;
    }

    @Override
    public void insert(User user) {
        // Hàm này để tương thích với code cũ nếu cần
        userDao.insert(user);
    }

    @Override
    public UserModel findByUsername(String username) {
        return findByUserName(username);
    }
}