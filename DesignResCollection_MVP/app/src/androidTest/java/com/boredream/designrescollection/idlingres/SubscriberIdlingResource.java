package com.boredream.designrescollection.idlingres;

import android.support.test.espresso.IdlingResource;

import rx.Observable;
import rx.functions.Action0;

public class SubscriberIdlingResource implements IdlingResource {

    private Observable observable;
    private ResourceCallback callback;
    private boolean isIdleNow = true;

    private SubscriberIdlingResource(Observable observable) {
        this.observable = observable;
        this.observable.doOnCompleted(new Action0() {
            @Override
            public void call() {
                callback.onTransitionToIdle();
            }
        });
//        this.webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == FINISHED && view.getTitle() != null && callback != null) {
//                    callback.onTransitionToIdle();
//                }
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                if (SubscriberIdlingResource.this.webView.getProgress() == FINISHED && callback != null) {
//                    callback.onTransitionToIdle();
//                }
//            }
//        });
    }

    @Override
    public String getName() {
        return "Subscriber idling resource";
    }

    @Override
    public boolean isIdleNow() {
        if (observable == null) return true;
        return isIdleNow;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}