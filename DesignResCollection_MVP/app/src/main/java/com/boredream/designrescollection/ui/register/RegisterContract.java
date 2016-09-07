package com.boredream.designrescollection.ui.register;

import com.boredream.designrescollection.base.BasePresenter;
import com.boredream.designrescollection.base.BaseView;
import com.boredream.designrescollection.entity.User;

public class RegisterContract {
    interface View extends BaseView<Presenter> {

        void loginSuccess(User user);

    }

    interface Presenter extends BasePresenter {

        void requestSms(String phone);

        void register();

    }
}
