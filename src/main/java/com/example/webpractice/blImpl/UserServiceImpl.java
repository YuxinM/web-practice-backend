package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.UserDAO;
import com.example.webpractice.bl.UserService;
import com.example.webpractice.po.User;
import com.example.webpractice.util.SessionManager;
import com.example.webpractice.vo.ResponseVO;
import com.example.webpractice.vo.UserInfoVO;
import com.example.webpractice.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 23:15
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;

    /**
     * 注册账号
     *
     * @param userVO
     * @return 返回注册用户的id
     */
    @Override
    public ResponseVO register(UserVO userVO) {

        String name = userVO.getUsername();
        if (userDAO.UserExists(name) != 0) {
            log.warn("用户名 {} 已存在", name);
            return ResponseVO.buildFailure("用户名已存在");
        }
        User user = new User(name, userVO.getPassword());
        int id = userDAO.save(user).getId();
        log.info("用户 {} 创建成功", name);
        return ResponseVO.buildSuccess(id);
    }

    /**
     * 登录账号
     *
     * @param userVO
     * @return 登录成功返回用户id
     */
    @Override
    public ResponseVO login(UserVO userVO) {
        User user = SessionManager.getLoginUser();
        if (user != null) {
            log.warn("同一个浏览器只能登录一个账号");
            return ResponseVO.buildFailure("同一个浏览器只能登录一个账号");
        }
        String name = userVO.getUsername();
        if (userDAO.UserExists(name) == 0) {
            log.warn("用户名 {} 不存在", name);
            return ResponseVO.buildFailure("用户名不存在");
        }
        user = userDAO.getLoginInfo(name);
        String password = userVO.getPassword();
        if (!user.getPassword().equals(password)) {
            return ResponseVO.buildFailure("密码错误，请重试");
        }
        SessionManager.initLoginState(user);
        log.info("用户 {} 已登录", name);
        return ResponseVO.buildSuccess(new UserInfoVO(user.getId(), user.getUsername()));
    }

    /**
     * 登出账号
     *
     * @param id
     * @return
     */
    @Override
    public ResponseVO logout(int id) {
        if (userDAO.UserExistsById(id) == 0) {
            return ResponseVO.buildFailure("用户不存在");
        }
        SessionManager.removeLoginState();
        log.info("用户{}退出登录",id);
        return ResponseVO.buildSuccess();
    }
}
