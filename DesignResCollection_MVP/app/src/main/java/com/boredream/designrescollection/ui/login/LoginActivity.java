package com.boredream.designrescollection.ui.login;


import android.os.Bundle;

import com.boredream.bdcodehelper.utils.ActivityUtils;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.net.HttpRequest;

public class LoginActivity extends BaseActivity {

    /**
     * 是否为验证登录,true-登录成功后,直接finish返回到来源页 false-登录成功后跳转到主页
     */
    private boolean checkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initExtras();
        initView();
    }

    private void initExtras() {
        checkLogin = getIntent().getBooleanExtra("checkLogin", false);
    }

    private void initView() {
        initBackTitle("登录");

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance(checkLogin);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.fl_content);
        }

        new LoginPresenter(loginFragment, HttpRequest.getInstance());
    }
}
