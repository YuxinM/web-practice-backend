package com.example.webpractice.config;

import com.example.webpractice.DAO.PaperDAO;

import java.io.File;

/**
 * 配置文件
 *
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

    /**
     * 存放正文临时文件夹
     */
    public static final String CONTENT = "content";

    /**
     * 存放附件临时文件夹
     */
    public static final String APPENDIX = "appendix";

    /**
     * 存放内化文件的临时文件夹
     */
    public static final String ANALYSE="analyse";

    /**
     * 存放pdf的临时文件夹
     */
    public static final String Pdf="pdf";


    public static void main(String[] args) {
        System.out.println(PROJECT_ABSOLUTE_PATH);


    }
}
