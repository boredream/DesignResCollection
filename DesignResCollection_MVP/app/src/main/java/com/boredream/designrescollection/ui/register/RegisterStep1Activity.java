package com.boredream.designrescollection.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.entity.User;

/**
 * 注册页面步骤一
 */
public class RegisterStep1Activity extends BaseActivity implements View.OnClickListener, RegisterContract.View {

    private RegisterContract.Presenter presenter;
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
        presenter = new RegisterPresenter(this);
        initBackTitle("注册");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(this);
    }

    private void next() {
        String phone = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        presenter.requestSms(phone, password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                next();
                break;
        }
    }

    @Override
    public void requestSmsSuccess(String phone, String password) {
        // 短信验证码发送成功后,跳转到短信验证页,同时传递所需数据
        Intent intent = new Intent(this, RegisterStep2Activity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    @Override
    public void registerSuccess(User user) {
        // do nothing
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void dismissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void showTip(String message) {
        showToast(message);
    }

}
