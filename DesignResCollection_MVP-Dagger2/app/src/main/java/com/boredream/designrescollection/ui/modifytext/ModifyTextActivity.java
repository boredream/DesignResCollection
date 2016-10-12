package com.boredream.designrescollection.ui.modifytext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;

/**
 * 修改文字内容输入框页面
 */
public class ModifyTextActivity extends BaseActivity implements ModifyTextContract.View {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_OLD_STRING = "oldString";

    public static final String RESULT_IS_MODIFY = "isModify";
    public static final String RESULT_NEW_STRING = "newString";

    private String title;
    private String oldString;

    private ModifyTextContract.Presenter presenter;
    private EditText et_input;

    public static void start(Activity context, int requestCode, String title, String oldString) {
        Intent intent = new Intent(context, ModifyTextActivity.class);
        intent.putExtra(ModifyTextActivity.EXTRA_TITLE, title);
        intent.putExtra(ModifyTextActivity.EXTRA_OLD_STRING, oldString);
        context.startActivityForResult(intent, requestCode);
    }

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
        oldString = intent.getStringExtra(EXTRA_OLD_STRING);
    }

    private void initView() {
        presenter = new ModifyTextPresenter(this);

        initBackTitle(title + "修改")
                .setRightText("保存")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });
        et_input = (EditText) findViewById(R.id.et_input);
    }

    private void initData() {
        if (oldString != null) {
            et_input.setText(oldString);
        }
    }

    private void submit() {
        String str = et_input.getText().toString().trim();
        presenter.modifyText(title, oldString, str);
    }

    @Override
    public void modifyTextSuccess(boolean isModify, String newString) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_NEW_STRING, newString);
        intent.putExtra(RESULT_IS_MODIFY, isModify);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setPresenter(ModifyTextContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void showTip(String message) {
        showToast(message);
    }
}
