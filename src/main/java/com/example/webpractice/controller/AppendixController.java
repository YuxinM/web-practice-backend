package com.example.webpractice.controller;

import com.example.webpractice.bl.AppendixService;
import com.example.webpractice.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    @PostMapping("/uploadAttachment/{paperId}")
    @ResponseBody
    public ResponseVO uploadAppendix(@PathVariable("paperId")int paperId,
                                     @RequestParam("attachmentFile")MultipartFile[] files){
        return appendixService.uploadAppendix(paperId,files);
    }

    @GetMapping("/getAttachmentList/{paperId}")
    @ResponseBody
    public ResponseVO getAppendix(@PathVariable("paperId")int paperId){

        return appendixService.getAppendix(paperId);

    }

    @DeleteMapping("deleteAttachment/{id}")
    @ResponseBody
    public ResponseVO deleteAppendix(@PathVariable("id")int id){
        return appendixService.deleteAppendix(id);
    }




}
