package com.boredream.designrescollection.ui.register;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.ErrorInfoUtils;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import rx.Observable;
import rx.Subscriber;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.View view;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void requestSms(final String phone, final String password) {
        if (StringUtils.isEmpty(phone)) {
            view.showTip("请输入用户名");
            return;
        }

        if (StringUtils.isEmpty(password) || password.length() < 6) {
            view.showTip("请设置登录密码，不少于6位");
            return;
        }

        view.showProgress();

        // FIXME 模拟获取短信接口
        Observable<String> observable = Observable.just("发送验证码咯~");
        ObservableDecorator.decorate(observable).subscribe(new Subscriber<String>() {
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
            public void onNext(String string) {
                if (!view.isActive()) {
                    return;
                }
                view.dismissProgress();

                view.requestSmsSuccess(phone, password);
            }
        });
    }

    @Override
    public void register(String phone, String password, String code) {
        if (StringUtils.isEmpty(code)) {
            view.showTip("请输入验证码");
            return;
        }

        view.showProgress();

        // 注册接口
        User user = new User();
        user.setMobilePhoneNumber(phone);
        user.setUsername(phone);
        user.setPassword(password);
        user.setSmsCode(code);
        Observable<User> observable = HttpRequest.getInstance().service.register(user);
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
                        UserInfoKeeper.setCurrentUser(user);

                        if (!view.isActive()) {
                            return;
                        }

                        view.dismissProgress();

                        view.registerSuccess(user);
                    }
                });
    }

}
