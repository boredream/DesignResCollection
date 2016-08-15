package com.boredream.designrescollection.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * 短信验证页面步骤一,用于注册和忘记密码的发送短信和信息填写
 */
public class PhoneValidateStep1Activity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button btn_next;

    /**
     * 0-注册 1-忘记密码
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_validate_step1);

        initExtras();
        initView();
    }

    private void initExtras() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
    }

    private void initView() {
        initBackTitle(type == 1 ? "重置密码" : "注册");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(this);
    }

    private void next() {
        // validate
        final String phone = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        final String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(this, "请设置登录密码，不少于6位", Toast.LENGTH_SHORT).show();
            return;
        }

        // validate success, do something
        requestSmsCode(phone, password);
    }

    /**
     * 发送短信验证码
     */
    private void requestSmsCode(final String phone, final String password) {
        showProgressDialog();
        Map<String, Object> params = new HashMap<>();
        params.put("mobilePhoneNumber", phone);
        Observable<Object> observable = HttpRequest.getApiService().requestSmsCode(params);
        ObservableDecorator.decorate(observable)
                .subscribe(new SimpleSubscriber<Object>(this) {
                    @Override
                    public void onNext(Object o) {
                        dismissProgressDialog();

                        // 短信验证码发送成功后,跳转到短信验证页,同时传递所需数据
                        Intent intent = new Intent(PhoneValidateStep1Activity.this, PhoneValidateStep2Activity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("phone", phone);
                        intent.putExtra("password", password);
                        startActivity(intent);
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
        switch (v.getId()) {
            case R.id.btn_next:
                next();
                break;
        }
    }
}
