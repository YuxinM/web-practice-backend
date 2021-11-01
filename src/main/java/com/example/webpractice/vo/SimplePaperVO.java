package com.example.webpractice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 法规简要信息列表
 *
 * @Author MengYuxin
 * @Date 2021/10/28 19:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplePaperVO {

    private int id;

    private String title;

    private String department;

    private String release_time;

    private String implement_time;

    private String grade;

    private boolean status;

    private String number;
}
