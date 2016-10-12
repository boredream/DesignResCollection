package com.boredream.bdcodehelper.present;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.boredream.bdcodehelper.adapter.ImageBrowserAdapter;

import java.util.ArrayList;

public class ImageBrowserPresent {
    private Activity activity;
    private ViewPager vp_image_brower;
    private TextView tv_image_index;

    private int position;
    private ImageBrowserAdapter adapter;
    private ArrayList<String> images;

    public ImageBrowserPresent(Activity activity, ImageBrowserAdapter adapter,
                               ViewPager vp_image_brower, TextView tv_image_index) {
        this.activity = activity;
        this.adapter = adapter;
        this.vp_image_brower = vp_image_brower;
        this.tv_image_index = tv_image_index;
    }

    public void init() {
        initData();
        setData();
    }

    private void initData() {
        images = (ArrayList<String>) activity.getIntent().getSerializableExtra("images");
        position = activity.getIntent().getIntExtra("position", 0);
    }

    private void setData() {
        vp_image_brower.setAdapter(adapter);

        final int size = images.size();
        int initPosition = position;

        if (size > 1) {
            tv_image_index.setVisibility(View.VISIBLE);
            tv_image_index.setText((position + 1) + "/" + size);
        } else {
            tv_image_index.setVisibility(View.GONE);
        }

        vp_image_brower.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int index = arg0 % size;
                tv_image_index.setText((index + 1) + "/" + size);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        vp_image_brower.setCurrentItem(initPosition);
    }
}
