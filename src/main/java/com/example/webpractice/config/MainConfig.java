package com.example.webpractice.config;

import java.io.File;

/**
 * 配置文件
 * @Author MengYuxin
 * @Date 2021/10/24 16:13
 */
public class MainConfig {


    /**
     * 动态的项目根路径 打包后指向war包同级目录
     */
    public static final String PROJECT_ABSOLUTE_PATH = new File("").getAbsolutePath();

    /**
     * 存放阿里云数据的临时文件夹
     */
    public static final String USER_DATA_DIR_NAME = "user_data";



    public static void main(String[] args) {
        System.out.println(PROJECT_ABSOLUTE_PATH);

    }
}
