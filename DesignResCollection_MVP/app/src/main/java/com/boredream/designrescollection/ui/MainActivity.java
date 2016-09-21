package com.boredream.designrescollection.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.fragment.FragmentController;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseActivity;
import com.boredream.designrescollection.ui.home.HomeFragment;
import com.boredream.designrescollection.utils.UpdateUtils;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_bottom_tab;
    private RadioButton rb1;
    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        setCouldDoubleBackExit(true);

        rg_bottom_tab = (RadioGroup) findViewById(R.id.rg_bottom_tab);
        rb1 = (RadioButton) findViewById(R.id.rb1);

        rg_bottom_tab.setOnCheckedChangeListener(this);
    }

    private void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new UserFragment());

        controller = new FragmentController(this, R.id.fl_content, fragments);

        // 默认Fragment
        rb1.setChecked(true);
        controller.showFragment(0);

        UpdateUtils.checkUpdate(this, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb1:
                controller.showFragment(0);
                break;
            case R.id.rb2:
                controller.showFragment(1);
                break;
        }
    }

}
