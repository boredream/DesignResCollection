package com.boredream.designrescollection.ui.userinfoedit;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;
import com.squareup.okhttp.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
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

        User user = new User();
        user.setObjectId("123456");
        presenter = new UserInfoEditPresenter(view, httpRequest, user);
    }

    @Test
    public void testFileUpload() throws Exception {
        FileUploadResponse response = new FileUploadResponse();
        response.setFilename("image");
        response.setUrl("www.baidu.com");

        when(httpRequest.fileUpload(any(byte[].class), anyString(), any(MediaType.class)))
                .thenReturn(Observable.just(response));

        // 如果不加updateUserById的when处理，由于httpRequest的是mock的对象，updateUserById方法会毫无反应
        // 但是updateUserById是ApiService方法，加空指针报错。
        // 因为虽然mock了httpRequest对象，但是没mock该对象中的service变量的对象
        when(httpRequest.service.updateUserById(anyString(), anyMap()))
                .thenReturn(Observable.just(new BaseEntity()));

        byte[] image = new byte[1024];
        presenter.uploadAvatar(image);

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).uploadUserInfoSuccess();
    }

    @Test
    public void testUpdateNickname() throws Exception {
        // 无法使用由于httpRequest.service，只能创建一个httpRequest方法使用之
        when(httpRequest.updateNickname(anyString(), anyString()))
                .thenReturn(Observable.just(new BaseEntity()));

        presenter.updateNickname("new name~~");

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).uploadUserInfoSuccess();
    }
}