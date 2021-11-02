package com.example.webpractice.bl.page;

import com.example.webpractice.vo.ResponseVO;

/**
 * @Author MengYuxin
 * @Date 2021/10/28 16:21
 * Interface
 */

public interface PageService {


    ResponseVO page(Integer pageNum, String title, String grade, String[] release_time, String[] implement_time, String[] department, String status);
}
