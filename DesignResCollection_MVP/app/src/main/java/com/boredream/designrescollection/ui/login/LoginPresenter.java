package com.boredream.designrescollection.ui.login;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.ErrorInfoUtils;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;

import rx.Observable;
import rx.Subscriber;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View view;
    private final HttpRequest httpRequest;

    public LoginPresenter(LoginContract.View view, HttpRequest httpRequest) {
        this.view = view;
        this.httpRequest = httpRequest;
        this.view.setPresenter(this);
    }

    @Override
    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            view.showTip("用户名不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            view.showTip("密码不能为空");
            return;
        }

        view.showProgress();

        Observable<User> observable = httpRequest.login(username, password);
        ObservableDecorator.decorate(observable).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!view.isActive()) {
                    return;
                }
                view.dismissProgress();

                String error = ErrorInfoUtils.parseHttpErrorInfo(e);
                view.showTip(error);
            }

            @Override
            public void onNext(User user) {
                if (!view.isActive()) {
                    return;
                }
                view.dismissProgress();

                view.loginSuccess(user);
            }
        });
    }
}
