package com.example.webpractice.bl;

import com.example.webpractice.vo.ResponseVO;
import com.example.webpractice.vo.UserVO;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 23:15
 * Interface
 */
public interface UserService {



    ResponseVO register(UserVO userVO);

    ResponseVO login(UserVO userVO);

    ResponseVO logout(int id);


}
