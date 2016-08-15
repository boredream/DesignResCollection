package com.boredream.bdcodehelper.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.bdcodehelper.utils.ToastUtils;

public class BoreBaseActivity extends AppCompatActivity {

    protected String TAG;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        TAG = getClass().getSimpleName();
        progressDialog = DialogUtils.createProgressDialog(this);
    }

    /**
     * 左侧有返回键的标题栏
     * <p>如果在此基础上还要加其他内容,比如右侧有文字按钮,可以获取该方法返回值继续设置其他内容
     *
     * @param title 标题
     */
    protected TitleBuilder initBackTitle(String title) {
        return new TitleBuilder(this)
                .setTitleText(title)
                .setLeftImage(R.mipmap.ic_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    /**
     * 跳转页面,无extra简易型
     *
     * @param tarActivity 目标页面
     */
    public void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    public void showToast(String msg) {
        ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
    }

    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

    public void showProgressDialog() {
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

}
