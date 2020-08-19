package com.jk.controller;

import com.jk.entity.HobbyEntity;
import com.jk.entity.UserEntity;
import com.jk.service.UserService;
import com.jk.service.UserServiceFeign;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController implements UserServiceFeign {

    @Resource
    private UserService userService;



    @Override
    @RequestMapping("/selectUserList")
    public List<UserEntity> selectUserList() {
        return userService.selectUserList();
    }

    @Override
    public List<HobbyEntity> typeSelect() {
        return null;
    }

    @Override
    public void saveUser(UserEntity user) {

    }

    @Override
    public UserEntity selectById(Integer userId) {
        return null;
    }

    @Override
    public void updateUser(UserEntity user) {

    }

    @Override
    public void deleteById(Integer userId) {

    }
}
