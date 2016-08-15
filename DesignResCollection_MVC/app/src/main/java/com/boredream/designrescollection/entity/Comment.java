package com.boredream.designrescollection.entity;


import com.boredream.designrescollection.base.BaseEntity;

public class Comment extends BaseEntity {
    /**
     * 所属资源
     */
    private DesignRes designRes;

    /**
     * 发送用户, Pointer or User
     */
    private User user;

    /**
     * 评论内容
     */
    private String content;

    public DesignRes getDesignRes() {
        return designRes;
    }

    public void setDesignRes(DesignRes designRes) {
        this.designRes = designRes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
