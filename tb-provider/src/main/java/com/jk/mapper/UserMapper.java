package com.jk.mapper;

import com.jk.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT u.user_id as userId ,u.user_name as userName , u.gender , u.birthday,h.hobby_name as hobbyName from t_user u LEFT JOIN t_hobby h ON u.hobby_id=h.hobby_id")
    List<UserEntity> selectUserList();
}
