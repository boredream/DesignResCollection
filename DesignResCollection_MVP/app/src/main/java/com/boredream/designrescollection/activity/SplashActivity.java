package com.boredream.designrescollection.activity;

import android.app.Activity;
import android.os.Bundle;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

    private static final int SPLASH_DUR_MIN_TIME = 2 * 1000;
    private static final int SPLASH_DUR_MAX_TIME = 5 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        autoLogin();
    }

    private void autoLogin() {
        String[] loginData = UserInfoKeeper.getLoginData();
        if (loginData == null) {
            // 不自动登录, 延迟2秒后跳转到主页
            delayIntentToActivity(SPLASH_DUR_MIN_TIME, MainActivity.class);
            return;
        }

        // 自动登录
        final long starTime = System.currentTimeMillis();
        Observable<User> observable = HttpRequest.loginByToken(loginData);
        ObservableDecorator.decorate(observable)
                // 设置超时时间,如果接口调用耗时超过最大值,也视为登录失败。防止页面停留时间过长
                .timeout(SPLASH_DUR_MAX_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        calculateDurTime(starTime);
                    }

                    @Override
                    public void onNext(User user) {
                        calculateDurTime(starTime);
                    }
                });
    }

    /**
     * 计算接口耗时,如果不足最小时间,则继续延迟补足后再跳转到主页。防止页面停留时间过短
     *
     * @param starTime 接口开始调用时间
     */
    private void calculateDurTime(long starTime) {
        long durTime = System.currentTimeMillis() - starTime;
        // 最小时间处理,防止过快的跳转页面, 至少为SPLASH_DUR_MIN_TIME
        long targetMilliseconds = Math.max(durTime, SPLASH_DUR_MIN_TIME);
        delayIntentToActivity(targetMilliseconds, MainActivity.class);
    }

    /**
     * 延迟跳转
     *
     * @param delayMilliseconds   延迟时间
     * @param targetActivityClass 跳转目标页面类,必须要是Activity的子类
     */
    private void delayIntentToActivity(long delayMilliseconds, Class<? extends Activity> targetActivityClass) {
        Observable.just(targetActivityClass)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(delayMilliseconds, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Class<? extends Activity>>() {
                    @Override
                    public void call(Class<? extends Activity> aClass) {
                        intent2Activity(aClass);
                    }
                });
    }
}
