package com.example.webpractice.bl;

import com.example.webpractice.vo.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author MengYuxin
 * @Date 2021/10/31 19:48
 * Interface
 */
public interface AppendixService {


    ResponseVO uploadAppendix(int paperId, MultipartFile[] files);

    ResponseVO getAppendix(int paperId);

    ResponseVO deleteAppendix(int id);

    void downloadAppendix(int id, HttpServletResponse response);
}
