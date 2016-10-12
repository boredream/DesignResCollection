package com.boredream.designrescollection.net;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.designrescollection.entity.User;

import org.junit.Before;
import org.junit.Test;

import rx.Subscriber;

public class HttpRequestTest {

    @Before
    public void setUp() {
        BoreConstants.isUnitTest = true;
    }

    @Test
    public void test() {
        HttpRequest.getInstance().login("18551681236", "123456").subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.toString());
            }

            @Override
            public void onNext(User user) {
                System.out.println(user.toString());
            }
        });
    }
}
