package com.boredream.designrescollection.entity;


import com.boredream.designrescollection.base.BaseEntity;

/**
 * 开盘时间
 */
public class OpenDate extends BaseEntity {

    private String building_num;
    private String open_date;
    private String openDate;
    private String finish_date;
    private String finishDate;
    private String community_id;
    private DesignRes community;

    public String getBuilding_num() {
        return building_num;
    }

    public void setBuilding_num(String building_num) {
        this.building_num = building_num;
    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
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
}
