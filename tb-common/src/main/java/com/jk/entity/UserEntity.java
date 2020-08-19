package com.jk.entity;/*
 *功能描述
 * @author wjt
 * @date
 * @param
 * @return
 */

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEntity implements Serializable {
    private Integer userId;

    private String userName;

    private String gender;

    private String birthday;

    private Integer hobbyId;

    private String  hobbyName;

}
