package com.boredream.designrescollection.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.ImageUtils;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.GlideHelper;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class UserInfoEditActivity extends BaseActivity implements View.OnClickListener {

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
        showUserAvatar();
        tv_username.setText(currentUser.getUsername());
    }

    private void showUserAvatar() {
        GlideHelper.showAvatar(this, currentUser.getAvatar(), iv_avatar);
    }

    private void initView() {
        initBackTitle("修改个人资料");

        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        ll_avatar = (LinearLayout) findViewById(R.id.ll_avatar);
        tv_username = (TextView) findViewById(R.id.tv_username);
        ll_username = (LinearLayout) findViewById(R.id.ll_username);

        ll_avatar.setOnClickListener(this);
        ll_username.setOnClickListener(this);
    }

    /**
     * 上传用户头像图片
     *
     * @param uri 头像图片uri
     */
    private void uploadUserAvatarImage(Uri uri) {
        showProgressDialog();

        // 第一步,上传头像文件到服务器
        HttpRequest.fileUpload(this, uri,
                iv_avatar.getWidth(), iv_avatar.getHeight(),
                new SimpleSubscriber<FileUploadResponse>(this) {
                    @Override
                    public void onNext(FileUploadResponse fileUploadResponse) {
                        // 第二步,将上传图片返回的url地址更新至用户对象中
                        uploadUserAvatarImage(HttpRequest.FILE_HOST + fileUploadResponse.getUrl());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 更新用户头像
     *
     * @param avatarUrl 头像图片地址
     */
    private void uploadUserAvatarImage(final String avatarUrl) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("avatar", avatarUrl);

        Observable<BaseEntity> observable = HttpRequest.getApiService().updateUserById(
                currentUser.getObjectId(), updateMap);
        ObservableDecorator.decorate(observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity entity) {
                        dismissProgressDialog();

                        // 成功后更新当前用户的头像数据
                        currentUser.setAvatar(avatarUrl);
                        UserInfoKeeper.setCurrentUser(currentUser);
                        showUserAvatar();

                        showToast("头像修改成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_avatar:
                ImageUtils.showImagePickDialog(this);
                break;
            case R.id.ll_username:
                intent2Activity(UsernameModifyActivity.class);
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
                uploadUserAvatarImage(ImageUtils.cropImageUri);
                break;
        }
    }

}
