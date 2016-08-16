package com.boredream.designrescollection.fragment;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.bdcodehelper.view.GridSpacingDecorator;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.adapter.DesignResAdapter;
import com.boredream.designrescollection.base.BaseFragment;
import com.boredream.designrescollection.constants.CommonConstants;
import com.boredream.designrescollection.entity.DesignRes;
import com.boredream.designrescollection.net.HttpRequest;

import java.util.ArrayList;

import rx.Observable;

public class HomeFragment extends BaseFragment {

    private View view;
    private MultiPageLoadPresent multiPageLoadPresent;
    private ArrayList<DesignRes> datas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_home, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        new TitleBuilder(view).setTitleText(getString(R.string.tab1));

        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent = new MultiPageLoadPresent(activity, view.findViewById(R.id.srl), pageIndex);

        multiPageLoadPresent.setItemDecoration(new GridSpacingDecorator(DisplayUtils.dp2px(activity, 8)));
        RecyclerView rv = multiPageLoadPresent.getRv();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        rv.setLayoutManager(gridLayoutManager);
    }

    private void initData() {
        loadData();
    }

    private void loadData() {
        datas.clear();

        DesignResAdapter adapter = new DesignResAdapter(activity, datas);
        multiPageLoadPresent.setRefreshing(true);
        multiPageLoadPresent.load(adapter, datas,
                new MultiPageRequest<ListResponse<DesignRes>>() {
                    @Override
                    public Observable<ListResponse<DesignRes>> request(int page) {
                        return HttpRequest.getDesignRes(page);
                    }
                }, null);
    }
}
