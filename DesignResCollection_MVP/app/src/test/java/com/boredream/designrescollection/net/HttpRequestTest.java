package com.boredream.designrescollection.net;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.designrescollection.entity.DesignRes;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.Subscriber;

public class HttpRequestTest {

    @Before
    public void setUp() {
        BoreConstants.isUnitTest = true;
    }

    @Test
    public void test() {
        Observable<ListResponse<DesignRes>> observable = HttpRequest.getInstance().getDesignRes(1);
        observable.subscribe(new Subscriber<ListResponse<DesignRes>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ListResponse<DesignRes> designResListResponse) {

            }
        });
    }
}
