package com.example.webpractice.bl;

import com.example.webpractice.vo.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 18:43
 * Interface
 */
public interface PaperService {


    /**
     * 根据id获取法规详情
     *
     * @param id
     * @return
     */
    ResponseVO getPaperById(int id);

    ResponseVO addPaper(String title, String number, String category, String department,
                        String grade, String release_time, String implement_time,
                        String interpret, String input_user, String input_time,
                        MultipartFile file, String status);

    ResponseVO abolish(String[] ids);

    ResponseVO publish(String[] ids);

    ResponseVO updatePaper(int id,String title, String number, String category, String department,
                        String grade, String release_time, String implement_time,
                        String interpret, String input_user, String input_time,
                        MultipartFile file, String status);
}
