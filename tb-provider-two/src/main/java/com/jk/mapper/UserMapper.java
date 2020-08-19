package com.jk.mapper;

import com.jk.entity.HobbyEntity;
import com.jk.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT u.user_id as userId ,u.user_name as userName ,u.hobby_id as hobbyId, u.gender , u.birthday,h.hobby_name as hobbyName from t_user u LEFT JOIN t_hobby h ON u.hobby_id=h.hobby_id")
    List<UserEntity> selectUserList();
    @Select("select hobby_id as hobbyId,hobby_name as hobbyName from t_hobby")
    List<HobbyEntity> typeSelect();
    @Insert("insert into t_user(birthday, gender, user_name,hobby_id) values(#{birthday},#{gender},#{userName},#{hobbyId})")
    void saveUser(UserEntity user);
    @Select("select * from t_user where user_id=#{value}")
    UserEntity selectById(Integer userId);
    @Update("update t_user set user_name = #{userName} , gender = #{gender},birthday=#{birthday},hobby_id=#{hobbyId} where user_id = #{userId}")
    void updateUser(UserEntity user);
    @Delete("delete from t_user where user_id=#{value}")
    void deleteById(Integer userId);
    @Insert("insert into t_hobby(hobby_name) values(#{hobbyName})")
    @Options(useGeneratedKeys=true, keyProperty="hobbyId", keyColumn="hobby_id")
    void saveHobby(HobbyEntity hobbyEntity);
}
