package com.example.webpractice.controller;

import com.example.webpractice.bl.PaperService;
import com.example.webpractice.po.Papers;
import com.example.webpractice.vo.PaperVO;
import com.example.webpractice.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 19:23
 */

@CrossOrigin
@Controller
@RequestMapping("/api/paper")
public class PaperController {


    @Autowired
    PaperService paperService;


    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseVO getPaperById(@PathVariable("id") int id) {
        return paperService.getPaperById(id);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseVO addPaper(@RequestBody PaperVO paperVO,
                               @RequestParam("file") MultipartFile file) {
        return null;
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseVO get(@RequestParam(value = "type", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "grade", required = false) String grade,
                          @RequestParam(value = "release_time", required = false) String[] release_time,
                          @RequestParam(value = "implement_time", required = false) String[] implement_time,
                          @RequestParam(value = "department", required = false) String department,
                          @RequestParam(value = "status", required = false) String status) {
        return null;
    }


}
