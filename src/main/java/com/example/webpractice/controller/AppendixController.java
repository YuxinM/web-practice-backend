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
                                     @RequestParam(value = "appendixFile", required = false) MultipartFile[] files) {
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

    @PostMapping("/downloadAppendix/{id}")
    @ResponseBody
    public void downloadAppendix(HttpServletResponse response, @PathVariable("id") int id) {
        appendixService.downloadAppendix(id, response);
    }


}
