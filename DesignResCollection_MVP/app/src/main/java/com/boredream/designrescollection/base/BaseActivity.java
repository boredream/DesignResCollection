package com.boredream.designrescollection.base;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.boredream.bdcodehelper.base.BoreBaseActivity;
import com.boredream.designrescollection.ui.MainActivity;
import com.boredream.designrescollection.constants.CommonConstants;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class BaseActivity extends BoreBaseActivity {

    public BaseApplication application;
    public SharedPreferences sp;

    private boolean couldDoubleBackExit;
    private boolean doubleBackExitPressedOnce;

    protected boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果是退出应用flag,则直接关闭当前页面,不加载UI
        boolean exit = getIntent().getBooleanExtra("exit", false);
        if (exit) {
            finish();
            return;
        }

        application = (BaseApplication) getApplication();
        sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    /**
     * 设置是否可以双击返回退出，需要有该功能的页面set true即可
     *
     * @param couldDoubleBackExit true-开启双击退出
     */
    public void setCouldDoubleBackExit(boolean couldDoubleBackExit) {
        this.couldDoubleBackExit = couldDoubleBackExit;
    }

    @Override
    public void onBackPressed() {
        if (!couldDoubleBackExit) {
            // 非双击退出状态，使用原back逻辑
            super.onBackPressed();
            return;
        }

        // 双击返回键关闭程序
        // 如果两秒重置时间内再次点击返回,则退出程序
        if (doubleBackExitPressedOnce) {
            exit();
            return;
        }

        doubleBackExitPressedOnce = true;
        showToast("再按一次返回键关闭程序");
        Observable.just(null)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // 延迟两秒后重置标志位为false
                        doubleBackExitPressedOnce = false;
                    }
                });
    }

    /**
     * 退出程序
     */
    protected void exit() {
        // 退出程序方法有多种
        // 这里使用clear + new task的方式清空整个任务栈,只保留新打开的Main页面
        // 然后Main页面接收到退出的标志位exit=true,finish自己,这样就关闭了全部页面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("exit", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
