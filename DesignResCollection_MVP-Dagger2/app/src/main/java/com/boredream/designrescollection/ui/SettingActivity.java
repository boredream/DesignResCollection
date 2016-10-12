package com.boredream.designrescollection.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.boredream.bdcodehelper.adapter.SettingRecyclerAdapter;
import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.utils.AppUtils;
import com.boredream.bdcodehelper.view.DividerItemDecoration;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.ui.feedback.FeedBackActivity;
import com.boredream.designrescollection.utils.UpdateUtils;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private RecyclerView rv_setting;
    private Button btn_logout;

    private SettingRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initView() {
        initBackTitle("设置");

        rv_setting = (RecyclerView) findViewById(R.id.rv_setting);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(this);
    }

    private void initData() {
        // 使用列表显示多个选项条
        List<SettingItem> items = new ArrayList<>();

        items.add(new SettingItem(
                R.mipmap.ic_cached_grey600_24dp,
                "检查更新",
                AppUtils.getAppVersionName(this),
                R.mipmap.ic_chevron_right_grey600_24dp
        ));
        items.add(new SettingItem(
                R.mipmap.ic_announcement_grey600_24dp,
                "反馈",
                null,
                R.mipmap.ic_chevron_right_grey600_24dp
        ));

        adapter = new SettingRecyclerAdapter(items, this);
        rv_setting.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rv_setting.setLayoutManager(linearLayoutManager);
        // 每个item之间的分割线
        rv_setting.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                showProgressDialog();
                // 强制检查更新,并添加额外回调用于处理进度框
                UpdateUtils.checkUpdate(this, true);
                break;
            case 1:
                if(UserInfoKeeper.checkLogin(this)) {
                    // 一般意见反馈不需要登录，这里模拟下需登录场景的处理方法
                    intent2Activity(FeedBackActivity.class);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                // 登出,清理用户数据同时跳转到主页
                UserInfoKeeper.logout();
                intent2Activity(MainActivity.class);
                break;
        }
    }
}
