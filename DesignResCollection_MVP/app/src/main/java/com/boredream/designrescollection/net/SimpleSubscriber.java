package com.boredream.designrescollection.net;


import android.content.Context;

import com.boredream.bdcodehelper.utils.ErrorInfoUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;

import rx.Subscriber;

/**
 * 通用订阅者,用于统一处理回调
 */
public class SimpleSubscriber<T> extends Subscriber<T> {

    private Context context;

    public SimpleSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onCompleted() {
        // sub
    }

    @Override
    public void onError(Throwable throwable) {
        // 统一处理错误回调，显示Toast
        String errorInfo = ErrorInfoUtils.parseHttpErrorInfo(throwable);
        ToastUtils.showToast(context, errorInfo);
    }

    @Override
    public void onNext(T t) {
        // sub
    }


}
