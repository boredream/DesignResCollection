package com.boredream.designrescollection.ui.feedback;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.entity.FeedBack;
import com.boredream.designrescollection.net.HttpRequest;

import rx.Observable;
import rx.Subscriber;

public class FeedBackPresenter implements FeedBackContract.Presenter {

    private final FeedBackContract.View view;
    private final HttpRequest.ApiService api;

    public FeedBackPresenter(FeedBackContract.View view, HttpRequest.ApiService api) {
        this.view = view;
        this.api = api;
        this.view.setPresenter(this);
    }

    @Override
    public void addFeedback(String content, String email) {
        // 开始验证输入内容
        if (StringUtils.isEmpty(content)) {
            view.showTip("反馈内容不能为空");
            return;
        }

        if (StringUtils.isEmpty(email)) {
            view.showTip("请输入邮箱地址,方便我们对您的意见进行及时回复");
            return;
        }

        view.showProgress();

        // 使用自定义对象存至云平台,作为简易版的反馈意见收集
        FeedBack fb = new FeedBack();
        fb.setContent(content);
        fb.setEmail(email);

        Observable<BaseEntity> observable = ObservableDecorator.decorate(api.addFeedBack(fb));
        observable.subscribe(new Subscriber<BaseEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!view.isActive()) {
                    return;
                }
                view.dismissProgress();

                view.showTip("反馈提交失败");
            }

            @Override
            public void onNext(BaseEntity entity) {
                if (!view.isActive()) {
                    return;
                }
                view.dismissProgress();

                view.addFeedbackSuccess();
            }
        });
    }
}
