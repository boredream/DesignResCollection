package com.boredream.designrescollection.fragment;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.boredream.bdcodehelper.adapter.LoadMoreAdapter;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.view.GridSpacingDecorator;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.adapter.DesignResAdapter;
import com.boredream.designrescollection.base.BaseFragment;
import com.boredream.designrescollection.constants.CommonConstants;
import com.boredream.designrescollection.entity.DesignRes;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class SearchFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_search;
    private ImageView iv_clear;

    private String searchKey;
    private MultiPageLoadPresent multiPageLoadPresent;
    private DesignResAdapter adapter;
    private ArrayList<DesignRes> datas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_search, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        et_search = (EditText) view.findViewById(R.id.et_search);
        iv_clear = (ImageView) view.findViewById(R.id.iv_clear);

        iv_clear.setOnClickListener(this);

        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent = new MultiPageLoadPresent(activity, view.findViewById(R.id.srl), pageIndex);

        multiPageLoadPresent.setItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
        RecyclerView rv = multiPageLoadPresent.getRv();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        rv.setLayoutManager(gridLayoutManager);

        initTextChangeListener();
    }

    /**
     * 初始化搜索监听,设置搜索规则
     */
    private Subscription searchSubscription;

    private void initTextChangeListener() {
        // 监听输入框文字变化
        RxTextView.textChanges(et_search)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, String>() { // 调用接口前先对数据进行预先处理
                    @Override
                    public String call(CharSequence charSequence) {
                        String str = charSequence == null ? "" : charSequence.toString().trim();

                        // 每次变化时,先清空当前列表
                        datas.clear();

                        // 利用加载更多已有的几种状态优化搜索显示效果
                        multiPageLoadPresent.setStatus(TextUtils.isEmpty(str)
                                ? LoadMoreAdapter.STATUS_NONE  // 如果文字为空,不做任何显示
                                : LoadMoreAdapter.STATUS_HAVE_MORE);  // 非空,会继续搜索,显示加载更多进度框
                        multiPageLoadPresent.notifyDataSetChanged();

                        // 取消订阅上一次的请求回调,防止输入新的内容后显示之前文字对应接口返回的数据
                        if (searchSubscription != null) {
                            searchSubscription.unsubscribe();
                        }

                        // 重置页数信息
                        multiPageLoadPresent.initPage();

                        // 获取输入框内文字,返回
                        return str;
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS) // * 防止连续快速输入时造成的多次调用接口
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<String>(activity) {
                    @Override
                    public void onNext(String s) {
                        // 调用搜索接口
                        loadData(s);
                    }
                });
    }

    /**
     * 发起搜索接口请求
     *
     * @param key 搜索关键字
     */
    private void loadData(final String key) {
        showLog("loadData: " + key);

        searchKey = key;

        // 关键字为空时,不做搜索
        if (TextUtils.isEmpty(key)) {
            return;
        }

        datas.clear();

        adapter = new DesignResAdapter(activity, datas);
        searchSubscription = multiPageLoadPresent.load(adapter, datas,
                new MultiPageRequest<ListResponse<DesignRes>>() {
                    @Override
                    public Observable<ListResponse<DesignRes>> request(int page) {
                        return HttpRequest.getDesignRes(page, searchKey);
                    }
                }, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                et_search.setText("");
                break;
        }
    }
}
