package com.example.webpractice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MengYuxin
 * @Date 2021/10/31 20:56
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppendixVO {
    
    private int id;

    private String name;

    private String size;

    private String creator;
}
