package com.boredream.designrescollection.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.entity.FeedBack;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;

import rx.Observable;

public class FeedBackActivity extends BaseActivity {

    private EditText et_content;
    private EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_back);

        initView();
    }

    private void initView() {
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
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "反馈内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入邮箱地址,方便我们对您的意见进行及时回复", Toast.LENGTH_SHORT).show();
            return;
        }

        // 使用自定义对象存至云平台,作为简易版的反馈意见收集
        FeedBack fb = new FeedBack();
        fb.setContent(content);
        fb.setEmail(email);

        showProgressDialog();
        Observable<BaseEntity> observable = HttpRequest.getApiService().addFeedBack(fb);
        ObservableDecorator.decorate(observable).subscribe(
                new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity entity) {
                        dismissProgressDialog();
                        showToast("反馈成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }
}