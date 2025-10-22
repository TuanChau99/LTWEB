package vn.iotstar.services.impl;

import vn.iotstar.daos.UserDao;
import vn.iotstar.daos.impl.UserDaolmpl;
import vn.iotstar.models.User;
import vn.iotstar.models.UserModel;
import vn.iotstar.services.UserService;

public class UserServiceImpl implements UserService {

    UserDao userDao = new UserDaolmpl();  // gọi DAO

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUserName(username);
        newUser.setFullName(fullname);
        newUser.setPassWord(password);
        newUser.setAvatar(null);
        newUser.setRoleid(5); // mặc định role user
        newUser.setPhone(phone);
        newUser.setCreatedDate(date);

        userDao.insert(newUser);
        return true;
    }

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

    @Override
    public UserModel login(String username, String password) {
        UserModel user = this.findByUserName(username);
        if (user != null) {
            // Nếu chưa mã hóa mật khẩu, so sánh trực tiếp
            if (password.equals(user.getPassword())) {
                return user;
            }
            // Nếu bạn dùng mã hóa (MD5/BCrypt) thì thay đoạn so sánh trên cho phù hợp
        }
        return null;
    }

    @Override
    public UserModel findByUserName(String username) {
        return userDao.findByUserName(username);
    }

	@Override
	public UserModel findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}
