package com.example.webpractice.controller;

import com.example.webpractice.bl.UserService;
import com.example.webpractice.vo.ResponseVO;
import com.example.webpractice.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 23:17
 */

@CrossOrigin
@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseVO register(@RequestBody UserVO userVO){
        return userService.register(userVO);
    }

    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserVO userVO){
        return userService.login(userVO);
    }

    @PostMapping("/logout/{id}")
    public ResponseVO logout(@PathVariable("id")int id){
        return userService.logout(id);
    }




}
