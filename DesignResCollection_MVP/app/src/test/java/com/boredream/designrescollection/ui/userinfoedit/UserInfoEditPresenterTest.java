package com.boredream.designrescollection.ui.userinfoedit;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.designrescollection.net.HttpRequest;
import com.squareup.okhttp.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserInfoEditPresenterTest {

    @Mock
    private UserInfoEditContract.View view;

    @Mock
    private HttpRequest httpRequest;

    private UserInfoEditPresenter presenter;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(view.isActive()).thenReturn(true);

        BoreConstants.isUnitTest = true;

        presenter = new UserInfoEditPresenter(view, httpRequest);
    }

    @Test
    public void test() throws Exception {
        FileUploadResponse response = new FileUploadResponse();
        response.setUrl("www.baidu.com");

        when(httpRequest.fileUpload(any(byte[].class), anyString(), any(MediaType.class)))
                .thenReturn(Observable.just(response));

        byte[] image = new byte[1024];
        presenter.uploadAvatar(image);

        verify(view).showProgress();
        verify(view).dismissProgress();
    }
}