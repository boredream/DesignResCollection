package com.boredream.designrescollection.ui.login;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.ErrorInfoUtils;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;

import rx.Observable;
import rx.Subscriber;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View rootView;

    public LoginPresenter(LoginContract.View rootView) {
        this.rootView = rootView;
        this.rootView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            rootView.showErrorToast("用户名不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            rootView.showErrorToast("密码不能为空");
            return;
        }

        rootView.showProgress();

        Observable<User> observable = HttpRequest.login(username, password);
        ObservableDecorator.decorate(observable).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!rootView.isActive()) {
                    return;
                }

                rootView.dismissProgress();

                String error = ErrorInfoUtils.parseHttpErrorInfo(e);
                rootView.showErrorToast(error);
            }

            @Override
            public void onNext(User user) {
                if (!rootView.isActive()) {
                    return;
                }

                rootView.dismissProgress();

                rootView.loginSuccess(user);
            }
        });
    }
}
