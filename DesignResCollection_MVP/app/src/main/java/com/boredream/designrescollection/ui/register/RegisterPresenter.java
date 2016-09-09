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

    private final RegisterContract.View rootView;

    public RegisterPresenter(RegisterContract.View rootView) {
        this.rootView = rootView;
        this.rootView.setPresenter(this);
    }

    @Override
    public void requestSms(final String phone, final String password) {
        if (StringUtils.isEmpty(phone)) {
            rootView.showLocalError("请输入用户名");
            return;
        }

        if (StringUtils.isEmpty(password) || password.length() < 6) {
            rootView.showLocalError("请设置登录密码，不少于6位");
            return;
        }

        rootView.showProgress();

        // FIXME 模拟获取短信接口
        Observable<String> observable = Observable.just("发送验证码咯~");
        ObservableDecorator.decorate(observable).subscribe(new Subscriber<String>() {
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
                rootView.showWebError(error);
            }

            @Override
            public void onNext(String string) {
                if (!rootView.isActive()) {
                    return;
                }
                rootView.dismissProgress();

                rootView.requestSmsSuccess(phone, password);
            }
        });
    }

    public Observable<User> getUserObservable(String phone, String password, String code) {
        User user = new User();
        user.setMobilePhoneNumber(phone);
        user.setUsername(phone);
        user.setPassword(password);
        user.setSmsCode(code);
        return ObservableDecorator.decorate(HttpRequest.getApiService().register(user));
    }

    @Override
    public void register(String phone, String password, String code) {
        if (StringUtils.isEmpty(code)) {
            rootView.showLocalError("请输入验证码");
            return;
        }

        rootView.showProgress();

        // 注册接口
        Observable<User> observable = getUserObservable(phone, password, code);
        observable.subscribe(new Subscriber<User>() {
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
                        rootView.showWebError(error);
                    }

                    @Override
                    public void onNext(User user) {
                        UserInfoKeeper.setCurrentUser(user);

                        if (!rootView.isActive()) {
                            return;
                        }

                        rootView.dismissProgress();

                        rootView.registerSuccess(user);
                    }
                });
    }

}
