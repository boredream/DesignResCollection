package com.boredream.designrescollection.ui.feedback;

import com.boredream.bdcodehelper.BoreConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FeedBackPresenterTest {
    private FeedBackPresenter presenter;

    @Mock
    private FeedBackContract.View view;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(view.isActive()).thenReturn(true);

        BoreConstants.isUnitTest = true;

        presenter = new FeedBackPresenter(view);
    }

    @Test
    public void testAddFeedback_Success() throws Exception {
        String content = "fuck it !!!!!!!!!!!!!!";
        String email = "110@qq.com";
        presenter.addFeedback(content, email);
        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).addFeedbackSuccess();
    }
}