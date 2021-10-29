package com.example.webpractice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.NamedEntityGraph;

/**
 * 分页查询条件VO
 * @Author MengYuxin
 * @Date 2021/10/28 16:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionVO {

    private int pageNum;

    private String title;

    private String grade;

    private String[] release_time;

    private String[] implement_time;

    private String department;

    private String status;
}
