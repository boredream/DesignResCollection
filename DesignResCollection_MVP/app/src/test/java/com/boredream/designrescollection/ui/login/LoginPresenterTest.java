package com.boredream.designrescollection.ui.login;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginPresenterTest {

    private LoginPresenter presenter;

    @Mock
    private LoginContract.View view;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(view.isActive()).thenReturn(true);

        BoreConstants.isUnitTest = true;

        presenter = new LoginPresenter(view);
    }

    @Test
    public void testEmptyPassword() throws Exception {
        presenter.login("13913391521", "");

        verify(view).showLocalError("密码不能为空");
    }

    @Test
    public void testEmptyUsername() throws Exception {
        presenter.login(null, "123456");

        verify(view).showLocalError("用户名不能为空");
    }

    @Test
    public void testLoginSuccess() throws Exception {
        presenter.login("18551681236", "123456");
        verify(view).showProgress();
        Thread.sleep(2000);
        verify(view).dismissProgress();
        verify(view).loginSuccess(UserInfoKeeper.getCurrentUser());
    }
}
