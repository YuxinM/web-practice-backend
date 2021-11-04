package com.example.webpractice.controller;

import com.example.webpractice.bl.AnalyseService;
import com.example.webpractice.vo.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author PanYue
 * @Date 2021/11/3 14:14
 */

@CrossOrigin
@Tag(name = "内规接口", description = "内化法规相关操作")
@Controller
@RequestMapping("/api/analyse")
public class AnalyseController {

    @Autowired
    AnalyseService analyseService;

    @Operation(summary = "获取内规详情", description = "根据ID获取内规详情")
    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseVO getAnalyseById(@PathVariable(value = "id") int id) {
        return analyseService.getAnalyseById(id);
    }

    @Operation(summary = "新增内规", description = "新增内规")
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

    @Operation(summary = "更新内规", description = "根据ID更新内规")
    @PutMapping("/update/{id}")
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

    @Operation(summary = "获取内规pdf文件",description = "根据ID获取文件")
    @PostMapping("/getFile/{id}")
    @ResponseBody
    public void getFile(@PathVariable("id")int id, HttpServletResponse response){
        analyseService.getFile(id,response);
    }

}
