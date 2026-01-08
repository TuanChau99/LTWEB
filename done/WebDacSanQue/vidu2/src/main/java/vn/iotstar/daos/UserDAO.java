package vn.iotstar.daos;

import java.util.List;

import vn.iotstar.models.User;
import vn.iotstar.models.UserModel;

public interface UserDAO {

	UserModel findByUserName(String username);
	UserModel findById(int id);
	void insert(User user);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
    
    List<UserModel> findAll();
	void delete(int id);
	void insert(UserModel user);
	void update(UserModel user);
	List<UserModel> getAllCustomers();
	void softDelete(int id);
	int countNewCustomers();
	List<UserModel> getDeletedUsers(); 
	void restoreUser(int id);
	void updateProfile(int id, String fullname, String email, String phone, String address);
}
