package com.boredream.designrescollection.ui.feedback;

import com.boredream.bdcodehelper.BoreConstants;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.entity.FeedBack;
import com.boredream.designrescollection.net.HttpRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 提交反馈很少有失败情况，所以使用mock模拟接口部分手动返回错误结果，以验证对应场景
 * 同时也提供真实接口的presenter，用于测试真实数据
 */
public class FeedBackPresenterTest {

    // 用于测试真实接口返回数据
    private FeedBackPresenter presenter;

    // 用于测试模拟接口返回数据
    private FeedBackPresenter mockPresenter;

    @Mock
    private FeedBackContract.View view;

    @Mock
    private HttpRequest.ApiService api;

    @Before
    public void setupMocksAndView() {
        // 使用Mock标签等需要先init初始化一下
        MockitoAnnotations.initMocks(this);

        // 当view调用isActive方法时，就返回true表示UI已激活。方便测试接口返回数据后测试view的方法
        when(view.isActive()).thenReturn(true);

        // 设置单元测试标识
        BoreConstants.isUnitTest = true;

        // 用真实接口创建反馈Presenter
        presenter = new FeedBackPresenter(view, HttpRequest.getInstance().service);
        // 用mock模拟接口创建反馈Presenter
        mockPresenter = new FeedBackPresenter(view, api);
    }

    @Test
    public void testAddFeedback_Success() throws Exception {
        // 真实数据，调用实际接口
        String content = "这个App真是好！";
        String email = "110@qq.com";
        presenter.addFeedback(content, email);

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).addFeedbackSuccess();
    }

    @Test
    public void testAddFeedback_Mock_Success() throws Exception {
        // 模拟数据，当api调用addFeedBack接口传入任意值时，就返回成功的基本数据BaseEntity
        when(api.addFeedBack(any(FeedBack.class))).thenReturn(Observable.just(new BaseEntity()));

        String content = "这个App真是棒！";
        String email = "119@qq.com";
        mockPresenter.addFeedback(content, email);

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).addFeedbackSuccess();
    }

    @Test
    public void testAddFeedback_Mock_Error() throws Exception {
        // 模拟数据，当api调用addFeedBack接口传入任意值时，就抛出错误error
        when(api.addFeedBack(any(FeedBack.class))).thenReturn(Observable.<BaseEntity>error(new Exception("孙贼你说谁辣鸡呢？")));

        String content = "这个App真是辣鸡！";
        String email = "120@qq.com";
        mockPresenter.addFeedback(content, email);

        verify(view).showProgress();
        verify(view).dismissProgress();
        verify(view).showTip("反馈提交失败");
    }
}