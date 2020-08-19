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
public class HobbyEntity implements Serializable {
    private Integer hobbyId;

    private String hobbyName;
}
