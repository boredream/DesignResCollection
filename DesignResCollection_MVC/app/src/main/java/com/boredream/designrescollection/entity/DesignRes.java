package com.boredream.designrescollection.entity;


import com.boredream.designrescollection.base.BaseEntity;

/**
 * 设计资源
 */
public class DesignRes extends BaseEntity {

    private String desc;
    private String srcTag;
    private String name;
    private String imgUrl;
    private String srcLink;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSrcTag() {
        return srcTag;
    }

    public void setSrcTag(String srcTag) {
        this.srcTag = srcTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSrcLink() {
        return srcLink;
    }

    public void setSrcLink(String srcLink) {
        this.srcLink = srcLink;
    }
}
