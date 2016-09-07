package com.boredream.designrescollection.ui.login;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginPresenterTest {
    private LoginPresenter presenter;

    @Mock
    private LoginContract.View loginView;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(loginView.isActive()).thenReturn(true);

        presenter = new LoginPresenter(loginView);
    }

    @Test
    public void testEmptyPassword() throws Exception {
        presenter.login("13913391521", "");

        verify(loginView).showErrorToast("密码不能为空");
    }

    @Test
    public void testEmptyUsername() throws Exception {
        presenter.login(null, "123456");

        verify(loginView).showErrorToast("用户名不能为空");
    }

    @Test
    public void testLoginSuccess() throws Exception {
        presenter.login("13913391521", "123456");

        verify(loginView).showProgress();
    }
}
