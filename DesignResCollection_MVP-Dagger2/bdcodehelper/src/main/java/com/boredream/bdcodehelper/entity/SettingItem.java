package com.boredream.bdcodehelper.entity;

/**
 * 更多item信息
 */
public class SettingItem {

    /**
     * 左侧图片
     */
    public int leftImgRes;

    /**
     * 中间文字
     */
    public String midText;

    /**
     * 右侧文字
     */
    public String rightText;

    /**
     * 右侧图片
     */
    public int rightImage;

    public SettingItem() {

    }

    public SettingItem(int leftImgRes, String midText, String rightText, int rightImage) {
        this.leftImgRes = leftImgRes;
        this.midText = midText;
        this.rightText = rightText;
        this.rightImage = rightImage;
    }
}
