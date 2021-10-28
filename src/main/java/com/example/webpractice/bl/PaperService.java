package com.example.webpractice.bl;

import com.example.webpractice.vo.ResponseVO;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 18:43
 * Interface
 */
public interface PaperService {


    /**
     * 根据id获取法规详情
     * @param id
     * @return
     */
    ResponseVO getPaperById(int id);
}
