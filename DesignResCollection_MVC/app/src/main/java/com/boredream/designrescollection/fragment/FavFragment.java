package com.boredream.designrescollection.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.adapter.DesignResAdapter;
import com.boredream.designrescollection.base.BaseFragment;
import com.boredream.designrescollection.constants.CommonConstants;
import com.boredream.designrescollection.entity.DesignRes;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.HttpRequest;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import java.util.ArrayList;

import rx.Observable;

public class FavFragment extends BaseFragment {
    private View view;
    private Button btn_login;

    private ArrayList<DesignRes> datas = new ArrayList<>();

    private User currentUser;
    private DesignResAdapter adapter;
    private MultiPageLoadPresent multiPageLoadPresent;
    private View srl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_fav, null);
        initView();
        return view;
    }

    private void initView() {
        new TitleBuilder(view).setTitleText("喜欢");

        srl = view.findViewById(R.id.srl);
        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent = new MultiPageLoadPresent(activity, srl, pageIndex);

        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 利用已有的方法直接跳转到登录页
                UserInfoKeeper.checkLogin(activity);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // 如果未登录进入本页面,然后跳转登录页面成功后返回,此时应该再次更新收藏列表
        currentUser = UserInfoKeeper.getCurrentUser();
        srl.setVisibility(currentUser == null ? View.GONE : View.VISIBLE);

        // 如果已经登录并且未加载到数据过,同时不在加载中,则去请求起始页数据
        if (currentUser != null && datas.size() == 0 && !multiPageLoadPresent.isRefreshing()) {
            // 手动调用下拉刷新
            multiPageLoadPresent.setRefreshing(true);
            loadData();
        }
    }

    /**
     * 加载收藏列表
     */
    private void loadData() {
        adapter = new DesignResAdapter(activity, datas);
        multiPageLoadPresent.load(adapter, datas,
                new MultiPageRequest<ListResponse<DesignRes>>() {
                    @Override
                    public Observable<ListResponse<DesignRes>> request(int page) {
                        return HttpRequest.getDesignRes(page);
                    }
                }, null);
    }

}
