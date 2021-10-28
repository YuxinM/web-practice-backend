package com.example.webpractice.vo;

import lombok.Data;

/**
 * @Author MengYuxin
 * @Date 2021/10/25 11:09
 * 返回给前端的VO
 */

@Data
public class ResponseVO {

    /**
     * 调用是否成功
     */
    private Boolean success;

    /**
     * 返回的提示信息
     */
    private String message;

    /**
     * 内容
     * 这里存放其他返回的VO
     */
    private Object content;

    public static ResponseVO buildSuccess(){
        ResponseVO response=new ResponseVO();
        response.setSuccess(true);
        return response;
    }

    public static ResponseVO buildSuccess(Object content){
        ResponseVO response=new ResponseVO();
        response.setContent(content);
        response.setSuccess(true);
        return response;
    }

    public static ResponseVO buildFailure(String message){
        ResponseVO response=new ResponseVO();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }




}
