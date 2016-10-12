package com.boredream.designrescollection.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseFragment;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.ui.MainActivity;
import com.boredream.designrescollection.ui.register.RegisterStep1Activity;

public class LoginFragment extends BaseFragment implements LoginContract.View, View.OnClickListener {

    private static final String ARGUMENT_CHECK_LOGIN = "checkLogin";
    private boolean checkLogin;

    private LoginContract.Presenter presenter;

    private EditText et_username;
    private EditText et_password;
    private TextView tv_forget_psw;
    private Button btn_login;
    private LinearLayout ll_regist;

    public static LoginFragment newInstance(boolean checkLogin) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(ARGUMENT_CHECK_LOGIN, checkLogin);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_login, null);
        initExtras();
        initView(view);
        return view;
    }

    private void initExtras() {
        checkLogin = getArguments().getBoolean(ARGUMENT_CHECK_LOGIN);
    }

    private void initView(View view) {
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_password = (EditText) view.findViewById(R.id.et_password);
        tv_forget_psw = (TextView) view.findViewById(R.id.tv_forget_psw);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        ll_regist = (LinearLayout) view.findViewById(R.id.ll_regist);

        tv_forget_psw.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        ll_regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, RegisterStep1Activity.class);
        switch (v.getId()) {
            case R.id.btn_login:
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                presenter.login(username, password);
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

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void dismissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loginSuccess(User user) {
        dismissProgressDialog();

        if (checkLogin) {
            activity.finish();
        } else {
            intent2Activity(MainActivity.class);
        }
    }

    @Override
    public void showTip(String message) {
        showToast(message);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

}
