package com.jk.service;

import com.jk.entity.HobbyEntity;
import com.jk.entity.UserEntity;
import com.jk.mapper.UserMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    @RequestMapping("/selectUserList")
    public List<UserEntity> selectUserList() {
        return userMapper.selectUserList();
    }

    @Override
    @RequestMapping("/typeSelect")
    public List<HobbyEntity> typeSelect() {
        return userMapper.typeSelect();
    }

    @Override
    @RequestMapping("/saveUser")
    public void saveUser(UserEntity user) {
        if(user.getHobbyName() != null && !"".equals(user.getHobbyName())){
            HobbyEntity hobbyEntity = new HobbyEntity();
            hobbyEntity.setHobbyName(user.getHobbyName());
            userMapper.saveHobby(hobbyEntity);
            user.setHobbyId(hobbyEntity.getHobbyId());
            userMapper.saveUser(user);
        }else{
            userMapper.saveUser(user);
        }
    }

    @Override
    @RequestMapping("/selectById/{userId}")
    public UserEntity selectById(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    @RequestMapping("/updateUser")
    public void updateUser(UserEntity user) {
        userMapper.updateUser(user);
    }

    @Override
    @RequestMapping("/deleteById/{userId}")
    public void deleteById(Integer userId) {
        userMapper.deleteById(userId);
    }

    @Override
    @RequestMapping("/saveOrder")
    public Object saveOrder(@RequestParam Integer userId, @RequestParam Integer productId) {
        Map<String, Object> orderMap = new HashMap<String, Object>();
        orderMap.put("orderId", 11111);
        orderMap.put("userId", userId);
        orderMap.put("productId", productId);
        orderMap.put("orderNo", "20200815103622123");
        orderMap.put("orderName", "一架辽宁舰");
        orderMap.put("orderPrice", 100000);
        orderMap.put("createTime", "2020-08-15 10:37");
        return orderMap;
    }

}
