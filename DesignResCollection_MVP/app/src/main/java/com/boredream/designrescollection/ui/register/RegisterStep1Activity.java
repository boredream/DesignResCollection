package com.boredream.designrescollection.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;

/**
 * 注册页面步骤一
 */
public class RegisterStep1Activity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step1);

        initView();
    }

    private void initView() {
        initBackTitle("注册");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(this);
    }

    private void next() {
        // validate
        final String username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        final String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(this, "请设置登录密码，不少于6位", Toast.LENGTH_SHORT).show();
            return;
        }

        // FIXME: 短信有限，所以直接模拟验证码发送成功，使用不验证的注册
        // requestSmsCode(phone, password);
        toValidateStep2(username, password);
    }

    // 短信验证码发送成功后,跳转到短信验证页,同时传递所需数据
    private void  toValidateStep2(String phone, String password) {
        Intent intent = new Intent(RegisterStep1Activity.this, RegisterStep2Activity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        startActivity(intent);
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
