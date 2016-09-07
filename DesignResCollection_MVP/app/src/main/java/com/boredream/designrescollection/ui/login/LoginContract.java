package com.boredream.designrescollection.ui.login;

import com.boredream.designrescollection.base.BasePresenter;
import com.boredream.designrescollection.base.BaseView;
import com.boredream.designrescollection.entity.User;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void loginSuccess(User user);

    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);

    }
}
