package com.boredream.designrescollection.ui.feedback;

import com.boredream.designrescollection.base.BasePresenter;
import com.boredream.designrescollection.base.BaseView;

public interface FeedBackContract {

    interface View extends BaseView<Presenter> {

        void addFeedbackSuccess();

    }

    interface Presenter extends BasePresenter {

        void addFeedback(String content, String email);

    }
}
