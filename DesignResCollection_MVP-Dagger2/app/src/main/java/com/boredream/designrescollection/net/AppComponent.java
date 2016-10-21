package com.boredream.designrescollection.net;

import com.boredream.designrescollection.base.BaseApplication;
import com.boredream.designrescollection.utils.PerApplication;

import javax.inject.Singleton;

import dagger.Component;

@PerApplication
@Singleton
@Component(modules = {ApiModule.class})
public interface AppComponent {

    void inject(BaseApplication request);

}
