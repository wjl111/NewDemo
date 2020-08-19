package com.jk.service;

import com.jk.entity.HobbyEntity;
import com.jk.entity.UserEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: tb-springcloud
 * @description: \
 * @author: 王江涛
 * @create: 2020-08-15 14:27
 */
@RestController
public interface UserService {

    @RequestMapping("/selectUserList")
    List<UserEntity> selectUserList();
    @RequestMapping("/typeSelect")
    List<HobbyEntity> typeSelect();
    @RequestMapping("/saveUser")
    void saveUser(@RequestBody UserEntity user);
    @RequestMapping("/selectById/{userId}")
    UserEntity selectById(@PathVariable Integer userId);
    @RequestMapping("/updateUser")
    void updateUser(@RequestBody UserEntity user);
    @RequestMapping("/deleteById/{userId}")
    void deleteById(@PathVariable Integer userId);
    @RequestMapping("/saveOrder")
    Object saveOrder(@RequestParam Integer userId, @RequestParam Integer productId);
}
