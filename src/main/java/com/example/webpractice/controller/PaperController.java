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
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 19:23
 */

@CrossOrigin
@Controller
@RequestMapping("/paper")
public class PaperController {


    @Autowired
    PaperService paperService;


    @GetMapping("get/{id}")
    public ResponseVO getPaperById(@PathVariable("id")int id){

        return paperService.getPaperById(id);
    }

    @PostMapping("/add")
    public ResponseVO addPaper(@RequestBody PaperVO paperVO,
                               @RequestParam("file")MultipartFile file){
        return null;
    }





}
