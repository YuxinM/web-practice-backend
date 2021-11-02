package com.example.webpractice.controller;

import com.example.webpractice.bl.PaperService;
import com.example.webpractice.bl.page.PageService;
import com.example.webpractice.po.Papers;
import com.example.webpractice.vo.PaperVO;
import com.example.webpractice.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import java.net.ServerSocket;
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

    @Autowired
    PageService pageService;


    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseVO getPaperById(@PathVariable("id") int id) {
        return paperService.getPaperById(id);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseVO addPaper(@RequestParam(value = "title") String title,
                               @RequestParam(value = "number", required = false) String number,
                               @RequestParam(value = "category") String category,
                               @RequestParam(value = "department") String department,
                               @RequestParam(value = "grade") String grade,
                               @RequestParam(value = "release_time") String release_time,
                               @RequestParam(value = "implement_time") String implement_time,
                               @RequestParam(value = "interpret_department", required = false) String interpret,
                               @RequestParam(value = "input_user") String input_user,
                               @RequestParam(value = "input_time") String input_time,
                               @RequestParam(value = "content") MultipartFile file,
                               @RequestParam(value = "status") String status,
                               @RequestParam(value = "analyse_status") String analyse_status
    ) {

        return paperService.addPaper(title, number, category, department, grade,
                release_time, implement_time, interpret, input_user, input_time, file, status, analyse_status);
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseVO get(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "grade", required = false) String grade,
                          @RequestParam(value = "release_time", required = false) String[] release_time,
                          @RequestParam(value = "implement_time", required = false) String[] implement_time,
                          @RequestParam(value = "department", required = false) String[] department,
                          @RequestParam(value = "status", required = false) String status) {


        return pageService.page(pageNum, title, grade, release_time, implement_time, department, status);
    }

    @PutMapping("/abolish")
    @ResponseBody
    public ResponseVO abolish(@RequestParam("ids") List<Integer> ids) {
        return paperService.abolish(ids);
    }

    @PutMapping("/publish")
    @ResponseBody
    public ResponseVO publish(@RequestParam("ids") List<Integer> ids) {
        return paperService.publish(ids);
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseVO update(@PathVariable("id") int id,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "number", required = false) String number,
                             @RequestParam(value = "category") String category,
                             @RequestParam(value = "department") String department,
                             @RequestParam(value = "grade") String grade,
                             @RequestParam(value = "release_time") String release_time,
                             @RequestParam(value = "implement_time") String implement_time,
                             @RequestParam(value = "interpret_department", required = false) String interpret,
                             @RequestParam(value = "input_user") String input_user,
                             @RequestParam(value = "input_time") String input_time,
                             @RequestParam(value = "content", required = false) MultipartFile file,
                             @RequestParam(value = "status") String status,
                             @RequestParam(value = "analyse_status") String analyse_status) {
        return paperService.updatePaper(id, title, number, category, department, grade,
                release_time, implement_time, interpret, input_user, input_time, file, status, analyse_status);
    }

    @DeleteMapping("/del")
    @ResponseBody
    public ResponseVO deletePaper(@RequestParam("ids") List<Integer> ids) {
        return paperService.delete(ids);

    }


}
