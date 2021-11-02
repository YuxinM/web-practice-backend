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

    private static final String ini="id";


    /**
     * 初始化登录状态
     *
     * @param user
     */
    public static void initLoginState(User user) {
        HttpSession session = getHttpSession();
        session.setAttribute(key, user);
        session.setAttribute(ini,user.getId());
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

    public static int getId(){
        HttpSession session=getHttpSession();
        return (int)session.getAttribute(ini);
    }


    /**
     * 移除用户的登录状态
     */
    public static void removeLoginState() {
        HttpSession session = getHttpSession();
        session.removeAttribute(key);
        session.removeAttribute(ini);
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
