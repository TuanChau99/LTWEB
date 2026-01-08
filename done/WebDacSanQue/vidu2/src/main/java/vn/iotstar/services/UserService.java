package vn.iotstar.services;

import java.util.List;

import vn.iotstar.models.User;
import vn.iotstar.models.UserModel;

public interface UserService {
	void insert(User user);
    boolean register(String username, String password, String email, String fullname, String phone);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
    UserModel login(String username, String password);
    UserModel findByUserName(String username);
	UserModel findByUsername(String username);
	
	List<UserModel> findAll(); // Cần thiết cho UserListController
    void insert(UserModel user); // Cần thiết cho UserAddController
    void update(UserModel user); // Dùng cho phần Edit sau này
    UserModel findById(int id);
    void delete(int id);
    
}
