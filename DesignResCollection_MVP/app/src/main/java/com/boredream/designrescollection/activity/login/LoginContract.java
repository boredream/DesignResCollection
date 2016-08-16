package com.boredream.designrescollection.activity.login;

import com.boredream.designrescollection.BasePresenter;
import com.boredream.designrescollection.BaseView;
import com.boredream.designrescollection.entity.User;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        void dismissProgress();

        void loginSuccess(User user);

        void showErrorToast(String message);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);

    }
}
