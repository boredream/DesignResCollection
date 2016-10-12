package com.boredream.designrescollection.ui.login;

import com.boredream.designrescollection.utils.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(modules = {LoginPresenterModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
