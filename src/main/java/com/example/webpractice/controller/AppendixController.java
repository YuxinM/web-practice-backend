package com.example.webpractice.controller;

import com.example.webpractice.bl.AppendixService;
import com.example.webpractice.vo.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "附件接口", description = "附件相关操作")
@Controller
@RequestMapping("/api/appendix")
public class AppendixController {

    @Autowired
    AppendixService appendixService;

    @Operation(summary = "上传附件", description = "根据法规ID上传附件列表")
    @PostMapping("/uploadAppendix/{paperId}")
    @ResponseBody
    public ResponseVO uploadAppendix(@PathVariable("paperId") int paperId,
                                     @RequestParam(value = "appendixFile", required = false) MultipartFile[] files) {
        return appendixService.uploadAppendix(paperId, files);
    }

    @Operation(summary = "获取附件列表", description = "根据法规ID获取附件列表")
    @GetMapping("/getAppendixList/{paperId}")
    @ResponseBody
    public ResponseVO getAppendix(@PathVariable("paperId") int paperId) {
        return appendixService.getAppendix(paperId);
    }

    @Operation(summary = "删除附件", description = "根据ID删除附件")
    @DeleteMapping("deleteAppendix/{id}")
    @ResponseBody
    public ResponseVO deleteAppendix(@PathVariable("id") int id) {
        return appendixService.deleteAppendix(id);
    }

    @Operation(summary = "下载附件", description = "根据ID下载附件")
    @PostMapping("/downloadAppendix/{id}")
    @ResponseBody
    public void downloadAppendix(@PathVariable("id") int id, HttpServletResponse response) {
        appendixService.downloadAppendix(id, response);
    }


}
