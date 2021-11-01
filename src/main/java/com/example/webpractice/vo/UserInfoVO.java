package com.example.webpractice.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名
     */
    private String username;

    public UserInfoVO(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
