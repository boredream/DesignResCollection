package com.boredream.designrescollection.ui.login;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 接口部分使用真实数据，只进行了view的mock测试，验证各种数据返回后的view的处理是否符合预期
 */
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

        // 设置标识，用于区分处理Observer的线程等情况
        BoreConstants.isUnitTest = true;

        presenter = new LoginPresenter(view, HttpRequest.getInstance());
    }

    @Test
    public void testLogin_EmptyPassword() throws Exception {
        presenter.login("13913391521", "");

        verify(view).showTip("密码不能为空");
    }

    @Test
    public void testLogin_EmptyUsername() throws Exception {
        presenter.login(null, "123456");

        verify(view).showTip("用户名不能为空");
    }

    @Test
    public void testLogin_Success() throws Exception {
        String phone = "18551681236";
        presenter.login(phone, "123456");

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).loginSuccess(UserInfoKeeper.getCurrentUser());
        Assert.assertEquals(UserInfoKeeper.getCurrentUser().getUsername(), phone);
    }

    @Test
    public void testLogin_UserNotExit() throws Exception {
        String phone = "110110110";
        presenter.login(phone, "123456");

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).showTip("找不到用户");
    }

    @Test
    public void testLogin_PswError() throws Exception {
        String phone = "18551681236";
        presenter.login(phone, "110119120");

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).showTip("密码不正确");
    }
}
