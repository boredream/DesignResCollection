package com.boredream.designrescollection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;

import rx.Observable;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_forget_psw;
    private LinearLayout ll_regist;

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

        et_username.setText("18551681236");
        et_password.setText("123456");
    }

    private void initExtras() {
        checkLogin = getIntent().getBooleanExtra("checkLogin", false);
    }

    private void initView() {
        initBackTitle("登录");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forget_psw = (TextView) findViewById(R.id.tv_forget_psw);
        ll_regist = (LinearLayout) findViewById(R.id.ll_regist);

        btn_login.setOnClickListener(this);
        tv_forget_psw.setOnClickListener(this);
        ll_regist.setOnClickListener(this);
    }

    private void login() {
        // validate
        String username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();
        Observable<User> observable = HttpRequest.login(username, password);
        ObservableDecorator.decorate(observable)
                .subscribe(new SimpleSubscriber<User>(this) {
                    @Override
                    public void onNext(User user) {
                        dismissProgressDialog();

                        if (checkLogin) {
                            // 是验证登录,登录成功后,直接finish返回到来源页
                            finish();
                        } else {
                            // 不是验证登录,登录成功后跳转到主页
                            intent2Activity(MainActivity.class);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PhoneValidateStep1Activity.class);
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_forget_psw:
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.ll_regist:
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
        }
    }
}