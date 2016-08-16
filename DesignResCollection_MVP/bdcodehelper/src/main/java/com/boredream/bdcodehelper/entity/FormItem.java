package com.boredream.bdcodehelper.entity;

import android.text.InputType;

public class FormItem {

    public static final int TYPE_INPUT = 0;
    public static final int TYPE_SELECT = 1;

    /**
     * 类型，0-输入，1-选择
     */
    public int type;

    /**
     * 左侧文字
     */
    public String leftText;

    /**
     * 中间文字，hint使用
     */
    public String midText;

    /**
     * 输入框类型
     */
    public int inputType;

    /**
     * 右侧图片id
     */
    public int rightImg;

    public FormItem() {

    }

    public static FormItem getInputItem(String leftText, String hint) {
        return getInputItem(leftText, hint, InputType.TYPE_CLASS_TEXT);
    }

    public static FormItem getInputItem(String leftText, String hint, int inputType) {
        FormItem formItem = new FormItem();
        formItem.type = TYPE_INPUT;
        formItem.leftText = leftText;
        formItem.midText = hint;
        formItem.inputType = inputType;
        return formItem;
    }

    public static FormItem getSelectItem(String leftText, int rightImg) {
        FormItem formItem = new FormItem();
        formItem.type = TYPE_SELECT;
        formItem.leftText = leftText;
        formItem.rightImg = rightImg;
        return formItem;
    }

}
