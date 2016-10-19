package com.boredream.designrescollection.ui.login;

import com.boredream.designrescollection.utils.PerActivity;

import dagger.Component;

@PerActivity
@Component(modules = {LoginPresenterModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
