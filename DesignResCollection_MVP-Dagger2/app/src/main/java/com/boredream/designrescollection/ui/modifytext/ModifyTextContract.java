package com.boredream.designrescollection.ui.modifytext;

import com.boredream.designrescollection.base.BasePresenter;
import com.boredream.designrescollection.base.BaseView;

public interface ModifyTextContract {

    interface View extends BaseView<Presenter> {

        void modifyTextSuccess(boolean isModify, String newString);

    }

    interface Presenter extends BasePresenter {

        void modifyText(String title, String oldString, String modifyString);

    }
}
