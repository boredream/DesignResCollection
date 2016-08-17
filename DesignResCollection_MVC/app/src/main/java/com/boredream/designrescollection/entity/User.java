package com.boredream.designrescollection.entity;


import com.boredream.designrescollection.base.BaseEntity;

public class User extends BaseEntity {

    private String sessionToken;

    private String nickname;

    private String username;

    /**
     * 验证手机号
     */
    private String mobilePhoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号验证码,发送短信验证时请求使用
     */
    private String smsCode;

    /**
     * 头像图片地址
     */
    private String avatar;

    private String company;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
