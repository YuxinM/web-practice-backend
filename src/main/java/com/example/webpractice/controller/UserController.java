package com.example.webpractice.controller;

import com.example.webpractice.bl.UserService;
import com.example.webpractice.util.SessionManager;
import com.example.webpractice.vo.ResponseVO;
import com.example.webpractice.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 23:17
 */

@CrossOrigin
@Tag(name = "用户接口", description = "用户账号相关操作")
@Controller
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    UserService userService;

    @Operation(summary = "注册", description = "新增用户账号")
    @PostMapping("/register")
    @ResponseBody
    public ResponseVO register(@RequestBody UserVO userVO) {
        return userService.register(userVO);
    }

    @Operation(summary = "登录", description = "用户登录账号")
    @PostMapping("/login")
    @ResponseBody
    public ResponseVO login(@RequestBody UserVO userVO) {
        return userService.login(userVO);
    }

    @Operation(summary = "登出", description = "用户登出账号")
    @PostMapping("/logout/{id}")
    @ResponseBody
    public ResponseVO logout(@PathVariable("id") int id) {
        return userService.logout(id);
    }


}
