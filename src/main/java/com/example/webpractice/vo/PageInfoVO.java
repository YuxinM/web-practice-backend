package com.example.webpractice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/31 14:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoVO {

    /**
     * 总数
     */
    private Long total;

    /**
     * 信息
     */
    private List<SimplePaperVO> list;


}
