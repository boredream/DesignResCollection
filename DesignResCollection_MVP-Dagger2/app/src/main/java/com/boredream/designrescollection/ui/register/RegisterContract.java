package com.boredream.designrescollection.ui.register;

import com.boredream.designrescollection.base.BasePresenter;
import com.boredream.designrescollection.base.BaseView;
import com.boredream.designrescollection.entity.User;

public class RegisterContract {
    interface View extends BaseView<Presenter> {

        void requestSmsSuccess(String phone, String password);

        void registerSuccess(User user);
    }

    interface Presenter extends BasePresenter {

        void requestSms(String phone, String password);

        void register(String phone, String password, String code);
    }
}
