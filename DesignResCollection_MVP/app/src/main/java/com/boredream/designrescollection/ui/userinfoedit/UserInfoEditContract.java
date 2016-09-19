package com.boredream.designrescollection.ui.userinfoedit;

import com.boredream.designrescollection.base.BasePresenter;
import com.boredream.designrescollection.base.BaseView;

public interface UserInfoEditContract {

    interface View extends BaseView<Presenter> {

        void uploadUserInfoSuccess();

    }

    interface Presenter extends BasePresenter {

        /**
         * 上传用户头像
         */
        void uploadAvatar(byte[] bytes);

        /**
         * 修改昵称
         */
        void updateNickname(String nickname);

    }
}
