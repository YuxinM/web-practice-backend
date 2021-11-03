package com.example.webpractice.vo;

import com.example.webpractice.po.Analyse;
import com.example.webpractice.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author PanYue
 * @Date 2021/11/3 14:48
 */
@Data
@NoArgsConstructor
public class AnalyseVO {
    private int id;
    private String title;
    private String number;
    private String category;
    private String interpret_department;
    private int input_user;
    private String input_time;
    private String content;
    private int paper_id;

    public AnalyseVO(Analyse analyse) {
        this.id = analyse.getId();
        this.title = analyse.getTitle();
        this.number = analyse.getPaper_number();
        this.category = analyse.getCategory();
        this.interpret_department = analyse.getInterpret_department();
        this.input_user = analyse.getUser_id();
        this.input_time = DateUtil.StampToDate(analyse.getInput_time());
        this.content = analyse.getContent();
        this.paper_id = analyse.getPaper_id();
    }
}
