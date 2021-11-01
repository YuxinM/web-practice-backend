package com.example.webpractice.bl.page;

import com.example.webpractice.vo.ConditionVO;
import com.example.webpractice.vo.ResponseVO;
import org.springframework.stereotype.Service;

/**
 * @Author MengYuxin
 * @Date 2021/10/28 16:21
 * Interface
 */

public interface PageService {


    ResponseVO page(Integer pageNum, String title, String grade, String[] release_time, String[] implement_time, String department, String status);
}
