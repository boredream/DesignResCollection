package com.boredream.designrescollection.net;

import com.boredream.designrescollection.utils.UserInfoKeeper;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class OkHttpModule {

    private static final String APP_ID_NAME = "X-LC-Id";
    private static final String API_KEY_NAME = "X-LC-Key";
    public static final String SESSION_TOKEN_KEY = "X-LC-Session";

    private static final String APP_ID_VALUE = "iaEH7ObIA4sPY8RSs3VCVXBg-gzGzoHsz";
    private static final String API_KEY_VALUE = "dXfhXIVyeWMN2czJkd4ehwzs";

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        // OkHttpClient
        OkHttpClient httpClient = new OkHttpClient();

        // 统一添加的Header
        httpClient.networkInterceptors().add(new Interceptor() {
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
        httpClient.interceptors().add(interceptor);

        return httpClient;
    }

//    @Singleton
//    @Provides
//    HttpRequest provideHttpRequest(OkHttpClient httpClient) {
//        return new HttpRequest(httpClient);
//    }

}
