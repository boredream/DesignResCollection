package com.boredream.designrescollection.net;

import com.boredream.designrescollection.base.BaseApplication;
import com.boredream.designrescollection.utils.PerActivity;

import javax.inject.Singleton;

import dagger.Component;

@PerActivity
@Singleton
@Component(modules = {OkHttpModule.class})
public interface HttpComponent {

    void inject(HttpRequest request);

    void inject(BaseApplication request);

}
