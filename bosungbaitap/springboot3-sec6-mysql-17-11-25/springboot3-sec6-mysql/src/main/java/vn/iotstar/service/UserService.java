package vn.iotstar.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;



import vn.iotstar.springboot3.entity.UserInfo;

import vn.iotstar.springboot3.repository.UserInfoRepository;





@Service

public record UserService(UserInfoRepository repository,

PasswordEncoder passwordEncoder) {

public String addUser(UserInfo userInfo) {

userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));

repository.save(userInfo);

return "Thêm user thành công!";

}

}