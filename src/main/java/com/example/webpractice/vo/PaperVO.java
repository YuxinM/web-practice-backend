package com.example.webpractice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author MengYuxin
 * @Date 2021/10/25 11:11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperVO {

    /**
     * 法规id
     * 主键 与原数据无关
     */
    private int id;

    /**
     * 法规标题
     */
    private String title;

    /**
     * 法规文号
     */
    private String number;

    /**
     * 外规类别
     */
    private String category;

    /**
     * 发文部门
     */
    private String department;

    /**
     * 发布时间
     */
    private String release_time;

    /**
     * 实施时间
     */
    private String implement_time;

    /**
     * 效力等级
     */
    private String grade;

    /**
     * 解读部门
     */
    private String interpret_department;

    /**
     * 录入人
     */
    private String input_user;

    /**
     * 录入时间
     */
    private String input_time;


    /**
     * 正文
     */
    private String content;

    /**
     * 状态
     * 未发布/已发布
     */
    private boolean status;

}
