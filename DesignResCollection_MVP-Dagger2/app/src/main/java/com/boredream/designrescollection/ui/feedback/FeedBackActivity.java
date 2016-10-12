package com.boredream.designrescollection.ui.feedback;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.net.HttpRequest;

public class FeedBackActivity extends BaseActivity implements FeedBackContract.View {

    private FeedBackContract.Presenter presenter;
    private EditText et_content;
    private EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_back);

        initView();
    }

    private void initView() {
        presenter = new FeedBackPresenter(this, HttpRequest.getInstance().service);

        initBackTitle("意见反馈")
                .setRightText("提交")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });

        et_content = (EditText) findViewById(R.id.et_content);
        et_email = (EditText) findViewById(R.id.et_email);

    }

    private void submit() {
        // 开始验证输入内容
        String content = et_content.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        presenter.addFeedback(content, email);
    }

    @Override
    public void addFeedbackSuccess() {
        showToast("反馈成功");
        finish();
    }

    @Override
    public void setPresenter(FeedBackContract.Presenter presenter) {
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