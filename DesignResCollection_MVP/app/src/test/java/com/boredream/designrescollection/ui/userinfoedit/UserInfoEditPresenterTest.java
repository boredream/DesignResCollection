package com.boredream.designrescollection.ui.userinfoedit;

import com.boredream.bdcodehelper.BoreConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class UserInfoEditPresenterTest {

    @Mock
    private UserInfoEditContract.View view;

    private UserInfoEditPresenter presenter;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(view.isActive()).thenReturn(true);

        BoreConstants.isUnitTest = true;

    }

    @Test
    public void test() throws Exception {
    }
}