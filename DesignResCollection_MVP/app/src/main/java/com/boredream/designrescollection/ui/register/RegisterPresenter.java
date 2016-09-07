package com.boredream.designrescollection.ui.register;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.View rootView;

    public RegisterPresenter(RegisterContract.View rootView) {
        this.rootView = rootView;
        this.rootView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void requestSms(String phone) {
        rootView.showProgress();

        // 模拟获取短信接口


    }

    @Override
    public void register() {

    }
}
