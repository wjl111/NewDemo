package com.jk.service;

import com.jk.entity.HobbyEntity;
import com.jk.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/error")
@Component
public class UserServiceFallback implements UserServiceFeign {



    @Override
    public List<UserEntity> selectUserList() {
        System.out.println("熔断处理，方法查询用户集合");
        return null;
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

    @Override
    public Object saveOrder(Integer userId, Integer productId) {
        System.out.println("进入 保存订单 熔断器类");
        return null;
    }
}
