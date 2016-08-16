package com.boredream.bdcodehelper.utils;

public class ViewUtils {

//    public static void setIndicator(Context context, final int size,
//                                    final ViewPager vp_banner, final RadioGroup rg_indicator) {
//        // 无图片和只有一张时不要indicator
//        if (size <= 1) {
//            rg_indicator.setVisibility(View.GONE);
//            return;
//        }
//        rg_indicator.setVisibility(View.VISIBLE);
//
//        vp_banner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                // ViewPager和RadioGroup联动
//                if (rg_indicator.getChildCount() > 1) {
//                    ((RadioButton) rg_indicator.getChildAt(position % size)).setChecked(true);
//                }
//            }
//        });
//
//        // 根据图片数量添加RadioButton
//        rg_indicator.removeAllViews();
//        for (int i = 0; i < size; i++) {
//            RadioButton rb = new RadioButton(context);
//            // TODO 圆形大小
//            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
//                    DisplayUtils.dp2px(context, 8), DisplayUtils.dp2px(context, 8));
//            if (i > 0) {
//                params.setMargins(DisplayUtils.dp2px(context, 8), 0, 0, 0);
//            }
//            rb.setLayoutParams(params);
//            rb.setButtonDrawable(new ColorDrawable());
//            rb.setBackgroundResource(R.drawable.shape_oval_yellow_stroke2solid_sel);
//            rb.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    // 圆不可点击选择
//                    return true;
//                }
//            });
//            rg_indicator.addView(rb);
//        }
//
//        // 默认选中第一个
//        if (rg_indicator.getChildCount() > 0) {
//            ((RadioButton) rg_indicator.getChildAt(0)).setChecked(true);
//        }
//    }

}
