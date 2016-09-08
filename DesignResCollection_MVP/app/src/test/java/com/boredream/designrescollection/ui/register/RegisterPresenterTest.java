package com.boredream.designrescollection.ui.register;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterPresenterTest {

    @Mock
    private RegisterContract.View view;

    @Mock
    private RegisterPresenter presenter;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(view.isActive()).thenReturn(true);

        BoreConstants.isUnitTest = true;

        presenter = new RegisterPresenter(view);
    }

    @Test
    public void testRequestSms() throws Exception {
        // 模拟发送短信，无需测试
    }

    @Captor
    private ArgumentCaptor<Subscriber<User>> captor;

    @Test
    public void testRegister() throws Exception {
        when(HttpRequest.getApiService().register(mock(User.class))).thenReturn();

        presenter.register("13913391234", "123456", "1234");
    }
}