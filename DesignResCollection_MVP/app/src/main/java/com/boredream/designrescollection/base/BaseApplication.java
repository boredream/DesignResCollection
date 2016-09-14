package com.boredream.designrescollection.base;


import android.app.Application;

import com.boredream.designrescollection.net.HttpRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initGlide();
    }

    /**
     * 图片加载框架Glide,使用OkHttp处理网络请求
     */
    private void initGlide() {
        OkHttpClient okHttpClient = HttpRequest.getInstance().getHttpClient();
        Glide.get(this).register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
