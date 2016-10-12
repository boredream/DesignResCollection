package com.boredream.designrescollection.ui.login;

import com.boredream.designrescollection.net.HttpRequest;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginPresenterModule {

    private final LoginContract.View mView;

    public LoginPresenterModule(LoginContract.View view) {
        mView = view;
    }

    @Provides
    LoginContract.View provideLoginContractView() {
        return mView;
    }

    @Provides
    HttpRequest provideHttpRequest() {
        return HttpRequest.getInstance();
    }

}
