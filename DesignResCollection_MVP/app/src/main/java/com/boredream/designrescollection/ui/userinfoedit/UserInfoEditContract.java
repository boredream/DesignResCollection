package com.boredream.designrescollection.ui.userinfoedit;

import android.net.Uri;

import com.boredream.designrescollection.base.BasePresenter;
import com.boredream.designrescollection.base.BaseView;

public interface UserInfoEditContract {

    interface View extends BaseView<Presenter> {

        void uploadAvatarSuccess();

    }

    interface Presenter extends BasePresenter {

        /**
         * 上传用户头像图片
         *
         * @param uri 头像图片uri
         */
        void uploadAvatar(Uri uri, int reqWidth, int reqHeight);

    }
}
