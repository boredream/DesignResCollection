package com.boredream.designrescollection.ui.feedback;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.ErrorInfoUtils;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.entity.FeedBack;
import com.boredream.designrescollection.net.HttpRequest;

import rx.Observable;
import rx.Subscriber;

public class FeedBackPresenter implements FeedBackContract.Presenter {

    private final FeedBackContract.View rootView;

    public FeedBackPresenter(FeedBackContract.View rootView) {
        this.rootView = rootView;
        this.rootView.setPresenter(this);
    }

    @Override
    public void addFeedback(String content, String email) {
        // 开始验证输入内容
        if (StringUtils.isEmpty(content)) {
            rootView.showLocalError("反馈内容不能为空");
            return;
        }

        if (StringUtils.isEmpty(email)) {
            rootView.showLocalError("请输入邮箱地址,方便我们对您的意见进行及时回复");
            return;
        }

        // 使用自定义对象存至云平台,作为简易版的反馈意见收集
        FeedBack fb = new FeedBack();
        fb.setContent(content);
        fb.setEmail(email);

        rootView.showProgress();

        Observable<BaseEntity> observable = HttpRequest.getApiService().addFeedBack(fb);
        ObservableDecorator.decorate(observable).subscribe(
                new Subscriber<BaseEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!rootView.isActive()) {
                            return;
                        }
                        rootView.dismissProgress();

                        String error = ErrorInfoUtils.parseHttpErrorInfo(e);
                        rootView.showWebError(error);
                    }

                    @Override
                    public void onNext(BaseEntity entity) {
                        if (!rootView.isActive()) {
                            return;
                        }
                        rootView.dismissProgress();

                        rootView.addFeedbackSuccess();
                    }
                });
    }
}
