package com.boredream.designrescollection.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * 修改文字内容输入框页面
 */
public class ModifyTextActivity extends BaseActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_OLD_STRING = "oldString";
    public static final String EXTRA_NEW_STRING = "newString";

    private String title;
    private String oldString;
    private String newString;

    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        Intent intent = getIntent();
        title = intent.getStringExtra(EXTRA_TITLE);
    }

    private void initView() {
        initBackTitle(title + "修改")
                .setRightText("保存")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });
        et = (EditText) findViewById(R.id.et);
    }

    private void initData() {
        if(oldString != null) {
            et.setText(oldString);
        }
    }

    private void submit() {

    }
}
