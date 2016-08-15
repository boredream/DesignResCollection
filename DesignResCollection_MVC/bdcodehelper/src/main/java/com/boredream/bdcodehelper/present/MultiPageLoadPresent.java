package com.boredream.bdcodehelper.present;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.adapter.LoadMoreAdapter;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.view.DividerItemDecoration;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class MultiPageLoadPresent {

    private Activity activity;
    private LoadMoreAdapter loadMoreAdapter;

    private SwipeRefreshLayout srl;
    private RecyclerView rv;
    private RecyclerView.ItemDecoration itemDecoration;

    public RecyclerView getRv() {
        return rv;
    }

    public MultiPageLoadPresent(Activity activity, View include_refresh_list, PageIndex pageIndex) {
        this.activity = activity;
        this.srl = (SwipeRefreshLayout) include_refresh_list;
        this.pageIndex = pageIndex;
        initView();
    }

    private void initView() {
        srl.setColorSchemeColors(activity.getResources().getColor(R.color.colorPrimary));
        rv = (RecyclerView) srl.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                activity, StaggeredGridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        itemDecoration = new DividerItemDecoration(activity);
        rv.addItemDecoration(itemDecoration);
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        rv.removeItemDecoration(this.itemDecoration);
        this.itemDecoration = itemDecoration;
        if (this.itemDecoration != null) {
            rv.addItemDecoration(this.itemDecoration);
        }
    }

    private ArrayList datas;
    private PageIndex pageIndex;
    private MultiPageRequest request;
    private Subscriber subscriber;

    public void setDatas(ArrayList datas) {
        this.datas = datas;
        rv.post(new Runnable() {
            @Override
            public void run() {
                loadMoreAdapter.notifyDataSetChanged();
            }
        });
    }

    public <T> Subscription load(RecyclerView.Adapter adapter,
                                 ArrayList datas,
                                 MultiPageRequest<T> request,
                                 Subscriber<T> subscriber) {
        this.datas = datas;
        this.request = request;
        this.subscriber = subscriber;

        loadMoreAdapter = new LoadMoreAdapter(rv, adapter,
                new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        // 列表拉到底部时,加载下一页
                        loadData(pageIndex.toNextPage());
                    }
                });
        rv.setAdapter(loadMoreAdapter);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 下拉刷新时,重新加载起始页
                loadData(pageIndex.toStartPage());
            }
        });

        return loadData(this.pageIndex.toStartPage());
    }

    public void setStatus(int status) {
        if(loadMoreAdapter != null) {
            loadMoreAdapter.setStatus(status);
        }
    }

    public void notifyDataSetChanged() {
        if(loadMoreAdapter != null) {
            loadMoreAdapter.notifyDataSetChanged();
        }
    }

    public void initPage() {
        pageIndex.init();
    }

    public boolean isRefreshing() {
        return srl.isRefreshing();
    }

    public void setRefreshing(final boolean refreshing) {
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(refreshing);
            }
        });
    }

    /**
     * 加载列表
     *
     * @param page 页数
     */
    private Subscription loadData(final int page) {
        Observable observable = this.request.request(page);
        return ObservableDecorator.decorate(observable).subscribe(
                new Subscriber<ListResponse>() {
                    @Override
                    public void onNext(ListResponse response) {
                        if (subscriber != null) {
                            subscriber.onNext(response);
                        }
                        setRefreshing(false);

                        // 加载成功后更新数据
                        pageIndex.setResponse(loadMoreAdapter, datas, response.getResults());
                    }

                    @Override
                    public void onCompleted() {
                        if (subscriber != null) {
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (subscriber != null) {
                            subscriber.onError(throwable);
                        }
                        setRefreshing(false);
                    }
                });
    }

}
