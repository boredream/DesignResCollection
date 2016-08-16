package com.boredream.designrescollection.activity.login;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.ErrorInfoUtils;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;

import rx.Observable;
import rx.Subscriber;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View loginView;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
        this.loginView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            loginView.showErrorToast("用户名不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            loginView.showErrorToast("密码不能为空");
            return;
        }

        loginView.showProgress();

        Observable<User> observable = HttpRequest.login(username, password);
        ObservableDecorator.decorate(observable).subscribe(
                new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!loginView.isActive()) {
                            return;
                        }

                        loginView.dismissProgress();

                        String error = ErrorInfoUtils.parseHttpErrorInfo(e);
                        loginView.showErrorToast(error);
                    }

                    @Override
                    public void onNext(User user) {
                        if (!loginView.isActive()) {
                            return;
                        }

                        loginView.dismissProgress();

                        loginView.loginSuccess(user);
                    }
                });
    }
}
