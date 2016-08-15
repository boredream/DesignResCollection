package com.boredream.designrescollection.entity;


import com.boredream.designrescollection.base.BaseEntity;

/**
 * 户型
 */
public class HouseType extends BaseEntity {

    private String typeName;
    private String photoPath;
    private String title;
    private String typeInfo;
    private String community_id;
    private DesignRes community;
    private float size;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public DesignRes getCommunity() {
        return community;
    }

    public void setCommunity(DesignRes community) {
        this.community = community;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }
}
