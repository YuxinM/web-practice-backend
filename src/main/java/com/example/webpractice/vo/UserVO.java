package com.example.webpractice.vo;

import lombok.Data;

/**
 * @Author MengYuxin
 * @Date 2021/10/25 11:47
 */
@Data
public class UserVO {

    /**
     * 用户id
     */
    private int id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;


}
