package com.boredream.designrescollection.ui.register;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegisterPresenterTest {

    @Mock
    private RegisterContract.View view;

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
        String phone = "13913391235";
        String password = "123456";
        presenter.requestSms(phone, password);

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).requestSmsSuccess(phone, password);
    }

    @Test
    public void testRegister() throws Exception {
        String phone = "13913391238";
        String password = "123456";
        String code = "1234";
        presenter.register(phone, password, code);

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).registerSuccess(UserInfoKeeper.getCurrentUser());
    }
}