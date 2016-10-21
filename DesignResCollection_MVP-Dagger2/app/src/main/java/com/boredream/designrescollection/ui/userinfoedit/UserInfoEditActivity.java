package com.boredream.designrescollection.ui.userinfoedit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.ImageUtils;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.ApiService;
import com.boredream.designrescollection.net.GlideHelper;
import com.boredream.designrescollection.ui.modifytext.ModifyTextActivity;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class UserInfoEditActivity extends BaseActivity implements View.OnClickListener, UserInfoEditContract.View {

    private static final int REQUEST_CODE_MODIFY_NICKNAME = 110;

    private UserInfoEditContract.Presenter presenter;
    @Inject ApiService service;

    private ImageView iv_avatar;
    private LinearLayout ll_avatar;
    private TextView tv_username;
    private LinearLayout ll_username;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);

        initView();
        initData();
    }

    private void initData() {
        currentUser = UserInfoKeeper.getCurrentUser();
        GlideHelper.showAvatar(this, currentUser.getAvatar(), iv_avatar);
        tv_username.setText(currentUser.getNickname());
    }

    private void initView() {
        presenter = new UserInfoEditPresenter(this, service, UserInfoKeeper.getCurrentUser());

        initBackTitle("修改个人资料");

        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        ll_avatar = (LinearLayout) findViewById(R.id.ll_avatar);
        tv_username = (TextView) findViewById(R.id.tv_username);
        ll_username = (LinearLayout) findViewById(R.id.ll_username);

        ll_avatar.setOnClickListener(this);
        ll_username.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_avatar:
                ImageUtils.showImagePickDialog(this);
                break;
            case R.id.ll_username:
                ModifyTextActivity.start(this, REQUEST_CODE_MODIFY_NICKNAME,
                        "昵称", currentUser.getNickname());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        Uri uri;
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                // 从相册选择
                uri = data.getData();
                compressAndUpload(uri);
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                // 相机拍照
                uri = ImageUtils.imageUriFromCamera;
                compressAndUpload(uri);
                break;
            case REQUEST_CODE_MODIFY_NICKNAME:
                boolean isModify = data.getBooleanExtra(ModifyTextActivity.RESULT_IS_MODIFY, true);
                if (!isModify) {
                    // 未修改，不做任何操作
                    return;
                }
                presenter.updateNickname(data.getStringExtra(ModifyTextActivity.RESULT_NEW_STRING));
                break;
        }
    }

    private void compressAndUpload(final Uri uri) {
        showProgress();

        Observable<byte[]> observable = ImageUtils.compressImage(this, uri, iv_avatar.getWidth(), iv_avatar.getHeight());
        ObservableDecorator.decorate(observable)
                .subscribe(new Subscriber<byte[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        dismissProgress();
                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        presenter.uploadAvatar(bytes);
                    }
                });
    }

    @Override
    public void uploadUserInfoSuccess() {
        initData();
    }

    @Override
    public void setPresenter(UserInfoEditContract.Presenter presenter) {
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
