package com.boredream.designrescollection.ui.modifytext;

import com.boredream.bdcodehelper.BoreConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ModifyTextPresenterTest {

    @Mock
    private ModifyTextContract.View view;

    private ModifyTextPresenter presenter;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(view.isActive()).thenReturn(true);

        BoreConstants.isUnitTest = true;

        presenter = new ModifyTextPresenter(view);
    }

    @Test
    public void testModifyText_Empty1() throws Exception {
        String title = "昵称";
        String oldString = "aaa";
        String modifyString = null;
        presenter.modifyText(title, oldString, modifyString);

        verify(view).showTip("昵称不能为空");
    }

    @Test
    public void testModifyText_Empty2() throws Exception {
        String title = "昵称";
        String oldString = "aaa";
        String modifyString = "";
        presenter.modifyText(title, oldString, modifyString);

        verify(view).showTip("昵称不能为空");
    }

    @Test
    public void testModifyText_NoModify() throws Exception {
        String title = "昵称";
        String oldString = "aaa";
        String modifyString = "aaa";
        presenter.modifyText(title, oldString, modifyString);

        verify(view).modifyTextSuccess(false, "aaa");
    }

    @Test
    public void testModifyText_Success1() throws Exception {
        String title = "昵称";
        String oldString = "aaa";
        String modifyString = "bbb";
        presenter.modifyText(title, oldString, modifyString);

        verify(view).modifyTextSuccess(true, "bbb");
    }

    @Test
    public void testModifyText_Success2() throws Exception {
        String title = "昵称";
        String oldString = null;
        String modifyString = "bbb";
        presenter.modifyText(title, oldString, modifyString);

        verify(view).modifyTextSuccess(true, "bbb");
    }

    @Test
    public void testModifyText_Success3() throws Exception {
        String title = "昵称";
        String oldString = "";
        String modifyString = "bbb";
        presenter.modifyText(title, oldString, modifyString);

        verify(view).modifyTextSuccess(true, "bbb");
    }

}