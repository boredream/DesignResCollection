package com.boredream.designrescollection.ui.userinfoedit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.utils.ImageUtils;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.GlideHelper;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.ui.ModifyTextActivity;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import rx.Subscriber;

public class UserInfoEditActivity extends BaseActivity implements View.OnClickListener, UserInfoEditContract.View {

    private UserInfoEditContract.Presenter presenter;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 如果跳转到二级页面修改了用户信息,则再次返回到该页面时显示最新的用户数据
        initData();
    }

    private void initData() {
        currentUser = UserInfoKeeper.getCurrentUser();
        GlideHelper.showAvatar(this, currentUser.getAvatar(), iv_avatar);
        tv_username.setText(currentUser.getNickname());
    }

    private void initView() {
        presenter = new UserInfoEditPresenter(this,
                HttpRequest.getInstance(), UserInfoKeeper.getCurrentUser());

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
                intent2Activity(ModifyTextActivity.class);
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
                // 选择后裁剪
                ImageUtils.cropImage(this, uri);
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                // 相机拍照
                uri = ImageUtils.imageUriFromCamera;
                // 拍照后裁剪
                ImageUtils.cropImage(this, uri);
                break;
            case ImageUtils.REQUEST_CODE_CROP_IMAGE:
                // 裁剪完成后上传图片
                compressAndUpload(ImageUtils.cropImageUri);
                break;
        }
    }

    private void compressAndUpload(Uri uri) {
        ImageUtils.compressImage(this, uri, iv_avatar.getWidth(), iv_avatar.getHeight(), new Subscriber<byte[]>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(byte[] bytes) {
                presenter.uploadAvatar(bytes);
            }
        });
    }

    @Override
    public void uploadAvatarSuccess() {
        GlideHelper.showAvatar(this, currentUser.getAvatar(), iv_avatar);
    }

    @Override
    public void updateNicknameSuccess() {
        tv_username.setText(currentUser.getNickname());
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
