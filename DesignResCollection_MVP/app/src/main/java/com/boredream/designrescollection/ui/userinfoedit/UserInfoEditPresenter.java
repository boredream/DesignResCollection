package com.boredream.designrescollection.ui.userinfoedit;

import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.utils.UserInfoKeeper;
import com.squareup.okhttp.MediaType;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

public class UserInfoEditPresenter implements UserInfoEditContract.Presenter {

    private final UserInfoEditContract.View view;
    private final HttpRequest httpRequest;

    public UserInfoEditPresenter(UserInfoEditContract.View view, HttpRequest httpRequest) {
        this.view = view;
        this.httpRequest = httpRequest;
        this.view.setPresenter(this);
    }

    @Override
    public void uploadAvatar(byte[] bytes) {
        view.showProgress();

        final String filename = "image_" + System.currentTimeMillis() + ".jpg";

        // 第一步,上传头像文件到服务器
        Observable<FileUploadResponse> observable = httpRequest.fileUpload(bytes, filename, MediaType.parse("image/jpeg"));
        ObservableDecorator.decorate(observable).subscribe(
                new Subscriber<FileUploadResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(FileUploadResponse fileUploadResponse) {
                        // 第二步,将上传图片返回的url地址更新至用户对象中
                        updateUserAvatar(HttpRequest.FILE_HOST + fileUploadResponse.getUrl());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.dismissProgress();
                    }
                });
    }

    /**
     * 更新用户头像
     *
     * @param avatarUrl 头像图片地址
     */
    public void updateUserAvatar(final String avatarUrl) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("avatar", avatarUrl);

        Observable<BaseEntity> observable = httpRequest.service.updateUserById(
                UserInfoKeeper.getCurrentUser().getObjectId(), updateMap);
        ObservableDecorator.decorate(observable)
                .subscribe(new Subscriber<BaseEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(BaseEntity entity) {
                        view.dismissProgress();

                        // 成功后更新当前用户的头像数据
                        UserInfoKeeper.getCurrentUser().setAvatar(avatarUrl);

                        view.uploadAvatarSuccess();

                        view.showTip("上传修改头像成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.dismissProgress();

                        view.showTip("上传修改头像失败");
                    }
                });
    }

}
