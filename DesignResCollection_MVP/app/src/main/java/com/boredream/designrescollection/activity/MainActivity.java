package com.boredream.designrescollection.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.fragment.FragmentController;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.fragment.FavFragment;
import com.boredream.designrescollection.fragment.HomeFragment;
import com.boredream.designrescollection.fragment.SearchFragment;
import com.boredream.designrescollection.fragment.UserFragment;
import com.boredream.designrescollection.utils.UpdateUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_bottom_tab;
    private RadioButton rb1;
    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 如果是退出应用flag,则直接关闭当前页面,不加载UI
        boolean exit = getIntent().getBooleanExtra("exit", false);
        if (exit) {
            finish();
            return;
        }

        initView();
        initData();
    }

    private void initView() {
        rg_bottom_tab = (RadioGroup) findViewById(R.id.rg_bottom_tab);
        rb1 = (RadioButton) findViewById(R.id.rb1);

        rg_bottom_tab.setOnCheckedChangeListener(this);
    }

    private void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new SearchFragment());
        fragments.add(new FavFragment());
        fragments.add(new UserFragment());
        controller = new FragmentController(this, R.id.fl_content, fragments);
        // 默认选择fragment
        rb1.setChecked(true);
        controller.showFragment(0);

        UpdateUtils.checkUpdate(this, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb1:
                controller.showFragment(0);
                break;
            case R.id.rb2:
                controller.showFragment(1);
                break;
            case R.id.rb3:
                controller.showFragment(2);
                break;
            case R.id.rb4:
                controller.showFragment(3);
                break;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        // 双击返回键关闭程序
        // 如果两秒重置时间内再次点击返回,则退出程序
        if (doubleBackToExitPressedOnce) {
            exit();
            return;
        }

        doubleBackToExitPressedOnce = true;
        showToast("再按一次返回键关闭程序");
        Observable.just(null)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // 延迟两秒后重置标志位为false
                        doubleBackToExitPressedOnce = false;
                    }
                });
    }

}
