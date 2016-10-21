package com.boredream.designrescollection.base;


import android.app.Application;

import com.boredream.designrescollection.net.DaggerAppComponent;
import com.boredream.designrescollection.utils.UserInfoKeeper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.io.InputStream;

public class BaseApplication extends Application {

    private static final String APP_ID_NAME = "X-LC-Id";
    private static final String API_KEY_NAME = "X-LC-Key";
    public static final String SESSION_TOKEN_KEY = "X-LC-Session";

    private static final String APP_ID_VALUE = "iaEH7ObIA4sPY8RSs3VCVXBg-gzGzoHsz";
    private static final String API_KEY_VALUE = "dXfhXIVyeWMN2czJkd4ehwzs";

    private OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();

        initOkHttpClient();
        initGlide();
    }

    private void initOkHttpClient() {
        // OkHttpClient
        okHttpClient = new OkHttpClient();

        // 统一添加的Header
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader(APP_ID_NAME, APP_ID_VALUE)
                        .addHeader(API_KEY_NAME, API_KEY_VALUE)
                        .addHeader(SESSION_TOKEN_KEY, UserInfoKeeper.getToken())
                        .build();
                return chain.proceed(request);
            }
        });

        // log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.interceptors().add(interceptor);
    }

    /**
     * 图片加载框架Glide,使用OkHttp处理网络请求
     */
    private void initGlide() {
        DaggerAppComponent.builder().build().inject(this);

        Glide.get(this).register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
