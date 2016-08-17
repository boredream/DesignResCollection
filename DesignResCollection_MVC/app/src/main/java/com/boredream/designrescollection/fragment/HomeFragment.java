package com.boredream.designrescollection.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdcodehelper.adapter.LoadMoreAdapter;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.bdcodehelper.view.GridSpacingDecorator;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.adapter.DesignResAdapter;
import com.boredream.designrescollection.base.BaseFragment;
import com.boredream.designrescollection.constants.CommonConstants;
import com.boredream.designrescollection.entity.DesignRes;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.net.SimpleSubscriber;

import java.util.ArrayList;

import rx.Observable;

public class HomeFragment extends BaseFragment {

    private View view;
    private SwipeRefreshLayout srl;
    private RecyclerView rv;

    private int curPage = 1;
    private ArrayList<DesignRes> datas = new ArrayList<>();
    private LoadMoreAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_home, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        new TitleBuilder(view).setTitleText(getString(R.string.tab1));

        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.addItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        rv.setLayoutManager(gridLayoutManager);

        adapter = new LoadMoreAdapter(rv,
                new DesignResAdapter(activity, datas),
                new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        loadData(curPage + 1);
                    }
                });
        rv.setAdapter(adapter);
    }

    private void initData() {
        showProgressDialog();
        loadData(1);
    }

    private void loadData(final int page) {
        Observable<ListResponse<DesignRes>> observable = HttpRequest.getDesignRes(page);
        ObservableDecorator.decorate(observable).subscribe(
                new SimpleSubscriber<ListResponse<DesignRes>>(activity) {
                    @Override
                    public void onNext(ListResponse<DesignRes> response) {
                        curPage = page;

                        srl.setRefreshing(false);
                        dismissProgressDialog();

                        if (page == 1) {
                            datas.clear();
                        }
                        setResponse(response);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                        srl.setRefreshing(false);
                        dismissProgressDialog();
                    }
                });
    }

    private void setResponse(ListResponse<DesignRes> response) {
        datas.addAll(response.getResults());

        // 设置是否已加载完全部数据状态
        adapter.setStatus(response.getResults().size() == CommonConstants.COUNT_OF_PAGE
                ? LoadMoreAdapter.STATUS_HAVE_MORE : LoadMoreAdapter.STATUS_LOADED_ALL);
        adapter.notifyDataSetChanged();
    }
}
