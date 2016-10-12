package com.boredream.designrescollection.ui.home;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.designrescollection.entity.DesignRes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomePresenterTest {

    private HomePresenter presenter;

    @Mock
    private HomeContract.View view;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(view.isActive()).thenReturn(true);

        BoreConstants.isUnitTest = true;

        presenter = new HomePresenter(view);
    }

    @Test
    public void testPullToLoadList() throws Exception {
        presenter.pullToLoadList();

        verify(view).dismissProgress();
        verify(view).loadListSuccess(1, presenter.datas);
        Assert.assertTrue(presenter.datas.size() > 0);
    }

    @Test
    public void testLoadList() throws Exception {
        presenter.loadList(1);

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).loadListSuccess(1, presenter.datas);
        Assert.assertTrue(presenter.datas.size() > 0);
    }

    @Test
    public void testLoadList_overPage() throws Exception {
        presenter.loadList(999);

        verify(view).dismissProgress();
        verify(view).loadListSuccess(999, Collections.<DesignRes>emptyList());
        Assert.assertFalse(presenter.datas.size() > 0);
    }
}