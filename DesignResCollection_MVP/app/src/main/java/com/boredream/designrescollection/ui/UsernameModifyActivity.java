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
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * 修改用户名输入框页面
 */
public class UsernameModifyActivity extends BaseActivity {

    private EditText et;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        initView();
        initData();
    }

    private void initView() {
        initBackTitle("昵称修改")
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
        currentUser = UserInfoKeeper.getCurrentUser();
        et.setText(currentUser.getUsername());
    }

    private void submit() {
        // validate
        final String username = et.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 修改用户的username
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("username", username);

        showProgressDialog();
        Observable<BaseEntity> observable = HttpRequest.getInstance().service
                .updateUserById(currentUser.getObjectId(), updateMap);
        ObservableDecorator.decorate(observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity entity) {
                        dismissProgressDialog();

                        // 修改成功后更新当前用户的昵称
                        currentUser.setUsername(username);
                        UserInfoKeeper.setCurrentUser(currentUser);

                        showToast("昵称修改成功");
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
