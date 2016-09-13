package com.boredream.designrescollection.net;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.designrescollection.entity.DesignRes;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;

public class HttpRequestTest {

    HttpRequest.ApiService service;

    @Before
    public void setUp() {
        service = HttpRequest.getApiService();
    }

    @Test
    public void test() {
        Observable<ListResponse<DesignRes>> observable = HttpRequest.getDesignRes(1);
    }
}
