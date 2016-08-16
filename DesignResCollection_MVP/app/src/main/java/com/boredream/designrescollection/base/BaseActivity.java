package com.boredream.designrescollection.base;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.boredream.bdcodehelper.base.BoreBaseActivity;
import com.boredream.designrescollection.activity.login.LoginActivity;
import com.boredream.designrescollection.activity.MainActivity;
import com.boredream.designrescollection.constants.CommonConstants;

public class BaseActivity extends BoreBaseActivity {

    public BaseApplication application;
    public SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (BaseApplication) getApplication();
        sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);
    }

    /**
     * 清空任务栈跳转至登录页
     */
    protected void clearIntent2Login() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
