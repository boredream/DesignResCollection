package com.boredream.designrescollection.idlingres;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.plugins.RxJavaObservableExecutionHook;

public class RxIdlingResource extends RxJavaObservableExecutionHook implements IdlingResource {

    private static final String TAG = "RxIdlingResource";

    private final AtomicInteger subscriptions = new AtomicInteger(0);

    private static RxIdlingResource INSTANCE;

    private ResourceCallback resourceCallback;

    private RxIdlingResource() {
        //private
    }

    public static RxIdlingResource get() {
        if (INSTANCE == null) {
            INSTANCE = new RxIdlingResource();
            Espresso.registerIdlingResources(INSTANCE);
        }
        return INSTANCE;
    }

    /* ======================== */
    /* IdlingResource Overrides */
    /* ======================== */

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public boolean isIdleNow() {
        int activeSubscriptionCount = subscriptions.get();
        return activeSubscriptionCount == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    /* ======================================= */
    /* RxJavaObservableExecutionHook Overrides */
    /* ======================================= */

    @Override
    public <T> Observable.OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance,
                                                          final Observable.OnSubscribe<T> onSubscribe) {
        int activeSubscriptionCount = subscriptions.incrementAndGet();
        Log.d(TAG, onSubscribe + " - onSubscribeStart: " + activeSubscriptionCount);

        return new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                onSubscribe.call(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                        onFinally(onSubscribe, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                        onFinally(onSubscribe, "onError");
                    }

                    @Override
                    public void onNext(T t) {
                        subscriber.onNext(t);
                    }
                });
            }
        };
    }

    private <T> void onFinally(Observable.OnSubscribe<T> onSubscribe, final String finalizeCaller) {
        int activeSubscriptionCount = subscriptions.decrementAndGet();
        Log.d(TAG, onSubscribe + " - " + finalizeCaller + ": " + activeSubscriptionCount);

        if (activeSubscriptionCount == 0) {
            Log.d(TAG, "onTransitionToIdle");
            resourceCallback.onTransitionToIdle();
        }
    }
}