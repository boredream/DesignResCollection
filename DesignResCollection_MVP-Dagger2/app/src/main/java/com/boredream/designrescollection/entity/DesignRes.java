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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DesignRes designRes = (DesignRes) o;

        if (desc != null ? !desc.equals(designRes.desc) : designRes.desc != null) return false;
        if (srcTag != null ? !srcTag.equals(designRes.srcTag) : designRes.srcTag != null)
            return false;
        if (name != null ? !name.equals(designRes.name) : designRes.name != null) return false;
        if (imgUrl != null ? !imgUrl.equals(designRes.imgUrl) : designRes.imgUrl != null)
            return false;
        return srcLink != null ? srcLink.equals(designRes.srcLink) : designRes.srcLink == null;

    }

    @Override
    public int hashCode() {
        int result = desc != null ? desc.hashCode() : 0;
        result = 31 * result + (srcTag != null ? srcTag.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (srcLink != null ? srcLink.hashCode() : 0);
        return result;
    }
}
