package com.boredream.designrescollection.net;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class ApiModule {

    // LeanCloud
    public static final String HOST = "https://api.leancloud.cn";

    @Singleton
    @Provides
    ApiService provideApiService(OkHttpClient httpClient) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // rxjava
                .client(httpClient)
                .build();

        return retrofit.create(ApiService.class);
    }

}
