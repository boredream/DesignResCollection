package com.boredream.bdcodehelper.present;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.adapter.BannerPagerAdapter;
import com.boredream.bdcodehelper.entity.ImageUrlInterface;
import com.boredream.bdcodehelper.utils.DisplayUtils;

import java.util.ArrayList;

public class ImageBannerPresent {

    private static final int AUTO_SCROLL_GAP_TIME = 4000;
    private static final int STATE_STOP = 0;
    private static final int STATE_AUTO_SCROLLING = 1;
    private int currentState;

    private Context context;
    private final CountDownTimer countDownTimer;

    private ArrayList<? extends ImageUrlInterface> images;
    private ViewPager vp_banner;

    private RadioGroup rg_indicator;
    private BannerPagerAdapter adapter;

    public ImageBannerPresent(Context context, View include_banner_with_indicator) {
        this.context = context;

        vp_banner = (ViewPager) include_banner_with_indicator.findViewById(R.id.vp_banner);
        rg_indicator = (RadioGroup) include_banner_with_indicator.findViewById(R.id.rg_indicator);

        vp_banner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                // FIXME
//                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//                    stopAutoScroll();
//                } else {
//                    startAutoScroll();
//                }
            }
        });

        countDownTimer = new CountDownTimer(
                Long.MAX_VALUE, AUTO_SCROLL_GAP_TIME) {
            @Override
            public void onTick(long l) {
                vp_banner.setCurrentItem(vp_banner.getCurrentItem() + 1);
            }

            @Override
            public void onFinish() {

            }
        };
    }

    public void load(ArrayList<? extends ImageUrlInterface> images) {
        this.images = images;

        adapter = new BannerPagerAdapter(context, images);
        vp_banner.setAdapter(adapter);

        setIndicator();
        startAutoScroll();
    }

    public void startAutoScroll() {
        if (currentState == STATE_AUTO_SCROLLING) {
            return;
        }

        currentState = STATE_AUTO_SCROLLING;
        countDownTimer.start();
    }

    public void stopAutoScroll() {
        currentState = STATE_STOP;
        countDownTimer.cancel();
    }

    private void setIndicator() {
        if (images.size() <= 1) {
            rg_indicator.setVisibility(View.GONE);
            return;
        }

        rg_indicator.setVisibility(View.VISIBLE);

        vp_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (rg_indicator.getChildCount() > 1) {
                    ((RadioButton) rg_indicator.getChildAt(position % images.size())).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rg_indicator.removeAllViews();
        for (int i = 0; i < images.size(); i++) {
            RadioButton rb = new RadioButton(context);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    DisplayUtils.dp2px(context, 8), DisplayUtils.dp2px(context, 8));
            if (i > 0) {
                params.setMargins(DisplayUtils.dp2px(context, 8), 0, 0, 0);
            }
            rb.setLayoutParams(params);
            rb.setButtonDrawable(new ColorDrawable());
            rb.setBackgroundResource(R.drawable.shape_oval_primary2white_sel);
            rb.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // do nothing
                    return true;
                }
            });
            rg_indicator.addView(rb);
        }

        ((RadioButton) rg_indicator.getChildAt(0)).setChecked(true);
    }
}
