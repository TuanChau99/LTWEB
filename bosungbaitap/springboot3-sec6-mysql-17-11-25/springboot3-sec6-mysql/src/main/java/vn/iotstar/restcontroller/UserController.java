package vn.iotstar.restcontroller;

import vn.iotstar.springboot3.entity.UserInfo;

import vn.iotstar.springboot3.service.UserService;



import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;



import lombok.RequiredArgsConstructor;





@RestController

@RequestMapping("/user")

@RequiredArgsConstructor

public class UserController {



private final UserService userService;



@PostMapping("/new")

public String addUser(@RequestBody UserInfo userInfo) {

return userService.addUser(userInfo);

}

}