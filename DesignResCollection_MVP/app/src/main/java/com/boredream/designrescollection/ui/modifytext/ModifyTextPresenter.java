package com.boredream.designrescollection.ui.modifytext;

import com.boredream.bdcodehelper.utils.StringUtils;

public class ModifyTextPresenter implements ModifyTextContract.Presenter {

    private final ModifyTextContract.View view;

    public ModifyTextPresenter(ModifyTextContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void modifyText(String title, String oldString, String modifyString) {
        if (StringUtils.isEmpty(modifyString)) {
            view.showTip(title + "不能为空");
            return;
        }

        // 旧文字为空或旧文字和新文字不相等，视为已修改
        boolean isModify = oldString == null || !oldString.equals(modifyString);
        view.modifyTextSuccess(isModify, modifyString);
    }
}
