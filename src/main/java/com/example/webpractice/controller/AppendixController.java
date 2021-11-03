package com.example.webpractice.controller;

import com.example.webpractice.bl.AppendixService;
import com.example.webpractice.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author MengYuxin
 * @Date 2021/10/31 19:32
 */

@CrossOrigin
@Controller
@RequestMapping("/api/appendix")
public class AppendixController {

    @Autowired
    AppendixService appendixService;


    @PostMapping("/uploadAppendix/{paperId}")
    @ResponseBody
    public ResponseVO uploadAppendix(@PathVariable("paperId") int paperId,
                                     @RequestParam("appendixFile") MultipartFile[] files) {
        return appendixService.uploadAppendix(paperId, files);
    }

    @GetMapping("/getAppendixList/{paperId}")
    @ResponseBody
    public ResponseVO getAppendix(@PathVariable("paperId") int paperId) {
        return appendixService.getAppendix(paperId);
    }

    @DeleteMapping("deleteAppendix/{id}")
    @ResponseBody
    public ResponseVO deleteAppendix(@PathVariable("id") int id) {
        return appendixService.deleteAppendix(id);
    }

    @GetMapping("/downloadAppendix/{id}")
    @ResponseBody
    public ResponseVO downloadAppendix(HttpServletResponse response, @PathVariable("id")int id){
        return appendixService.downloadAppendix(id,response);
    }


}
