package com.example.webpractice.bl.prepare;

import com.example.webpractice.vo.ResponseVO;

/**
 * 运行之前的一些准备
 * @Author MengYuxin
 * @Date 2021/11/3 23:29
 * Interface
 */
public interface BeforeRunService {


    void deleteFile();

    void deleteOssFile();
}
