package com.example.webpractice.controller;

import com.example.webpractice.bl.AnalyseService;
import com.example.webpractice.bl.PaperService;
import com.example.webpractice.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author PanYue
 * @Date 2021/11/3 14:14
 */

@CrossOrigin
@Controller
@RequestMapping("/api/analyse")
public class AnalyseController {

    @Autowired
    AnalyseService analyseService;

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseVO getAnalyseById(@PathVariable(value = "id") int id) {
        return analyseService.getAnalyseById(id);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseVO addPaper(@RequestParam(value = "title") String title,
                               @RequestParam(value = "number", required = false) String number,
                               @RequestParam(value = "category") String category,
                               @RequestParam(value = "interpret_department", required = false) String interpret,
                               @RequestParam(value = "input_user") String input_user,
                               @RequestParam(value = "input_time") String input_time,
                               @RequestParam(value = "content", required = false) MultipartFile file,
                               @RequestParam(value = "paper_id") String paper_id
    ) {
        return analyseService.addAnalyse(title, number, category, interpret, input_user, input_time, file, paper_id);
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseVO update(@PathVariable("id") int id,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "number", required = false) String number,
                             @RequestParam(value = "category") String category,
                             @RequestParam(value = "interpret_department", required = false) String interpret,
                             @RequestParam(value = "input_user") String input_user,
                             @RequestParam(value = "input_time") String input_time,
                             @RequestParam(value = "content", required = false) MultipartFile file) {
        return analyseService.updateAnalyse(id, title, number, category, interpret, input_user, input_time, file);
    }

}
