package com.example.webpractice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2022/1/2 20:46
 * 这是一个法规的上位下位法以及废除法规的关系
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationVO {

    /*
    本法规是根据什么法规制定的
     */
    private List<String>pre;

    /*
    什么法规是根据本法规制定的
     */
    private List<String>post;

    /*
    本法规废除了什么法规
     */
    private List<String>abolish;
}
