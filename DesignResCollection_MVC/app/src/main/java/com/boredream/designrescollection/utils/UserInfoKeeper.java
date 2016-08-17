package com.boredream.designrescollection.utils;


import com.boredream.designrescollection.entity.User;

/**
 * 用户信息保存工具
 */
public class UserInfoKeeper {

    private static User currentUser;

    /**
     * 获取当前登录用户
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * 保存设置当前登录用户
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * 清空当前登录用户
     */
    public static void clearCurrentUser() {
        currentUser = null;
    }

    public static String getToken() {
        // 统一Header配置时用的token,没有的话要用空字符串,不能为null
        String token = "";
        if (currentUser != null && currentUser.getSessionToken() != null) {
            token = currentUser.getSessionToken();
        }
        return token;
    }

    /**
     * 登出
     */
    public static void logout() {
        clearCurrentUser();
    }

}
