package com.boredream.designrescollection.utils;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.boredream.designrescollection.activity.login.LoginActivity;
import com.boredream.designrescollection.base.BaseApplication;
import com.boredream.designrescollection.constants.CommonConstants;
import com.boredream.designrescollection.entity.User;
import com.google.gson.Gson;

/**
 * 用户信息保存工具
 */
public class UserInfoKeeper {
    private static final String SP_KEY_CURRENT_USER = "current_user";
    private static final String SP_KEY_USER_ID = "user_id";
    private static final String SP_KEY_TOKEN = "token";

    private static User currentUser;

    /**
     * 获取当前登录用户,先从缓存中获取,获取不到时从sp中获取
     */
    public static User getCurrentUser() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        String userJson = sp.getString(SP_KEY_CURRENT_USER, null);
        if (currentUser == null && !TextUtils.isEmpty(userJson)) {
            currentUser = new Gson().fromJson(userJson, User.class);
        }
        return currentUser;
    }

    /**
     * 保存设置当前登录用户,缓存和sp中都进行保存
     */
    public static void setCurrentUser(User user) {
        if (user != null) {
            SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                    CommonConstants.SP_NAME, Context.MODE_PRIVATE);
            String userJson = new Gson().toJson(user);
            sp.edit().putString(SP_KEY_CURRENT_USER, userJson).apply();
        }
        currentUser = user;
    }

    /**
     * 清空当前登录用户,同时清空缓存和sp中信息
     */
    public static void clearCurrentUser() {
        currentUser = null;
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(SP_KEY_CURRENT_USER).apply();
    }

    /**
     * 保存当前用户的登录信息,用于自动登录
     *
     * @param userid 用户id
     * @param token  用户口令
     */
    public static void saveLoginData(String userid, String token) {
        // 正常逻辑应该是直接用token去获取当前用户信息,不需要id,但是接口没有提供获取当前登录用户信息接口
        if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(token)) {
            return;
        }

        // 保存在sp中,不像是账号密码敏感信息,无需加密
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SP_KEY_USER_ID, userid)
                .putString(SP_KEY_TOKEN, token)
                .apply();
    }

    /**
     * 获取当前用户的登录信息,用于自动登录
     *
     * @return [0]-用户userid [1]-用户口令token, 未保存或只保存一者时都返回null
     */
    public static String[] getLoginData() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        String userid = sp.getString(SP_KEY_USER_ID, null);
        String token = sp.getString(SP_KEY_TOKEN, null);
        if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(token)) {
            return null;
        }

        String[] loginData = new String[]{userid, token};
        return loginData;
    }

    /**
     * 清空登录信息
     */
    public static void clearLoginData() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(
                CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(SP_KEY_USER_ID)
                .remove(SP_KEY_TOKEN)
                .apply();
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
     * 登出,同时清空用户信息和登录信息
     */
    public static void logout() {
        clearCurrentUser();
        clearLoginData();
    }

    /**
     * 检测登录状态
     *
     * @return true-已登录 false-未登录,会自动跳转至登录页
     */
    public static boolean checkLogin(Context context) {
        if (currentUser == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("checkLogin", true);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

}
