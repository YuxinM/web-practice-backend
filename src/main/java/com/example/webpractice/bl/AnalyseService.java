package com.example.webpractice.bl;

import com.example.webpractice.vo.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author PanYue
 * @Date 2021/11/3 14:10
 * Interface
 */
public interface AnalyseService {


    /**
     * 根据id获取内化法规详情
     *
     * @param id
     * @return
     */
    ResponseVO getAnalyseById(int id);

    ResponseVO addAnalyse(String title, String number, String category, String interpret,
                          String input_user, String input_time, MultipartFile file, String paper_id);

    ResponseVO updateAnalyse(int id, String title, String number, String category, String interpret,
                             String input_user, String input_time, MultipartFile file, String paper_id);

}
