package com.example.webpractice.util;

import com.example.webpractice.po.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 会话的session管理类
 *
 * @Author MengYuxin
 * @Date 2021/10/27 23:00
 */
public class SessionManager {

    private static final String key = "login";


    /**
     * 初始化登录状态
     *
     * @param user
     */
    public static void initLoginState(User user) {
        HttpSession session = getHttpSession();
        session.setAttribute(key, user);
    }

    /**
     * 得到登录的用户
     *
     * @return
     */
    public static User getLoginUser() {
        HttpSession session = getHttpSession();
        return (User) session.getAttribute(key);
    }


    /**
     * 移除用户的登录状态
     */
    public static void removeLoginState() {
        HttpSession session = getHttpSession();
        session.removeAttribute(key);
        session.invalidate();
    }


    /**
     * 获取用户httpsession
     *
     * @return
     */
    public static HttpSession getHttpSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getSession();
    }


}
