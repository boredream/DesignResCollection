package com.boredream.bdcodehelper.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.boredream.bdcodehelper.R;

import java.text.DecimalFormat;

public class StringUtils {

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String getMoney(float cost) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return "￥" + decimalFormat.format(cost);
    }

    public static class PrimaryClickableSpan extends ClickableSpan {

        private Context context;

        public PrimaryClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {
            // do nothing
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.colorPrimary));
            ds.setUnderlineText(false);
        }
    }
}
