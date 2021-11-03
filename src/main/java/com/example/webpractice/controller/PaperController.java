package com.example.webpractice.controller;

import com.example.webpractice.bl.PaperService;
import com.example.webpractice.bl.page.PageService;
import com.example.webpractice.vo.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 19:23
 */

@CrossOrigin
@Tag(name = "外规接口", description = "外规相关操作")
@Controller
@RequestMapping("/api/paper")
public class PaperController {

    @Autowired
    PaperService paperService;

    @Autowired
    PageService pageService;

    @Operation(summary = "获取法规", description = "根据ID获取法规")
    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseVO getPaperById(@PathVariable("id") int id) {
        return paperService.getPaperById(id);
    }

    @Operation(summary = "新增法规", description = "新增法规")
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
                               @RequestParam(value = "content", required = false) MultipartFile file,
                               @RequestParam(value = "status") String status,
                               @RequestParam(value = "analyse_id") String analyse_id
    ) {
        return paperService.addPaper(title, number, category, department, grade,
                release_time, implement_time, interpret, input_user, input_time, file, status, analyse_id);
    }

    @Operation(summary = "获取法规", description = "分页获取法规列表")
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

    @Operation(summary = "废止法规", description = "根据ID列表废止法规")
    @PutMapping("/abolish")
    @ResponseBody
    public ResponseVO abolish(@RequestParam("ids") List<Integer> ids) {
        return paperService.abolish(ids);
    }

    @Operation(summary = "发布法规", description = "根据ID列表发布法规")
    @PutMapping("/publish")
    @ResponseBody
    public ResponseVO publish(@RequestParam("ids") List<Integer> ids) {
        return paperService.publish(ids);
    }

    @Operation(summary = "更新法规", description = "根据ID更新法规")
    @PutMapping("/update/{id}")
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
                             @RequestParam(value = "analyse_id") String analyse_id) {
        return paperService.updatePaper(id, title, number, category, department, grade,
                release_time, implement_time, interpret, input_user, input_time, file, status, analyse_id);
    }

    @Operation(summary = "删除法规", description = "根据ID列表删除法规")
    @DeleteMapping("/del")
    @ResponseBody
    public ResponseVO deletePaper(@RequestParam("ids") List<Integer> ids) {
        return paperService.delete(ids);

    }

    @Operation(summary = "获取法规统计数据", description = "获取法规统计数据")
    @GetMapping("/getStatisticalData")
    @ResponseBody
    public ResponseVO getStatisticalData() {
        return paperService.getStatisticalData();
    }

    @Operation(summary = "获取最近内化法规", description = "获取最近内化的3条法规")
    @GetMapping("/getRecentAnalyzed")
    @ResponseBody
    public ResponseVO getRecentAnalyzedPapers() {
        return paperService.getRecentAnalyzedPapers();
    }

}
