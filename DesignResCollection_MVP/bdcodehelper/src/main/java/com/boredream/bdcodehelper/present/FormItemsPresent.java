package com.boredream.bdcodehelper.present;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.entity.FormItem;
import com.boredream.bdcodehelper.utils.ToastUtils;

import java.util.Arrays;
import java.util.List;

public class FormItemsPresent {

    public interface OnSelectItemClickListener {
        void onSelectItemClick(String leftText);
    }

    private Context context;
    private LinearLayout ll_container;
    private List<FormItem> items;

    public FormItemsPresent(LinearLayout ll_container) {
        this.context = ll_container.getContext();
        this.ll_container = ll_container;
    }

    public void load(List<FormItem> items, Integer[] emptyIndexs,
                     final OnSelectItemClickListener listener) {
        this.items = items;
        List<Integer> emptyIndexList = Arrays.asList(emptyIndexs);

        for (int i = 0; i < items.size(); i++) {
            final FormItem formItem = items.get(i);

            // item
            View itemView;
            if (formItem.type == FormItem.TYPE_INPUT) {
                // input
                itemView = View.inflate(context, R.layout.formitem_input, null);
                EditText tv_mid = (EditText) itemView.findViewById(R.id.tv_mid);
                tv_mid.setInputType(formItem.inputType);
                tv_mid.setHint(formItem.midText);
            } else {
                // select
                itemView = View.inflate(context, R.layout.formitem_select, null);

                ImageView iv_right = (ImageView) itemView.findViewById(R.id.iv_right);
                if (formItem.rightImg != -1) {
                    iv_right.setImageResource(formItem.rightImg);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onSelectItemClick(formItem.leftText);
                        }
                    }
                });
            }
            TextView tv_left = (TextView) itemView.findViewById(R.id.tv_left);
            tv_left.setText(formItem.leftText);
            itemView.setTag(formItem.leftText);
            ll_container.addView(itemView);

            // divider
            View dividerView;
            if (emptyIndexList.contains(i)) {
                dividerView = View.inflate(context, R.layout.include_group_divider, null);
            } else {
                dividerView = new View(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                dividerView.setLayoutParams(params);
                dividerView.setBackgroundResource(R.color.divider_gray);
            }
            ll_container.addView(dividerView);
        }
    }

    public ViewGroup getItemView(String leftText) {
        ViewGroup itemView = (ViewGroup) ll_container.findViewWithTag(leftText);
        return itemView;
    }

    public String getString(String leftText) {
        ViewGroup itemView = getItemView(leftText);
        TextView tv_mid = (TextView) itemView.findViewById(R.id.tv_mid);
        return tv_mid.getText().toString().trim();
    }

    public LinearLayout showMidContainer(String leftText) {
        ViewGroup itemView = getItemView(leftText);
        LinearLayout ll_mid_container = (LinearLayout) itemView.findViewById(R.id.ll_mid_container);
        TextView tv_mid = (TextView) itemView.findViewById(R.id.tv_mid);
        ll_mid_container.setVisibility(View.VISIBLE);
        tv_mid.setVisibility(View.GONE);
        return ll_mid_container;
    }

    public TextView showMidText(String leftText, String midText) {
        ViewGroup itemView = getItemView(leftText);
        LinearLayout ll_mid_container = (LinearLayout) itemView.findViewById(R.id.ll_mid_container);
        TextView tv_mid = (TextView) itemView.findViewById(R.id.tv_mid);
        ll_mid_container.setVisibility(View.GONE);
        tv_mid.setVisibility(View.VISIBLE);
        tv_mid.setText(midText);
        return tv_mid;
    }

    public boolean validate() {
        for (FormItem item : items) {
            ViewGroup itemView = getItemView(item.leftText);
            if (item.type == FormItem.TYPE_INPUT) {
                // input
                EditText tv_mid = (EditText) itemView.findViewById(R.id.tv_mid);
                if (TextUtils.isEmpty(tv_mid.getText().toString().trim())) {
                    ToastUtils.showToast(context, tv_mid.getHint());
                    return false;
                }
            } else {
                // select
                TextView tv_mid = (TextView) itemView.findViewById(R.id.tv_mid);
                LinearLayout ll_mid_container = (LinearLayout) itemView.findViewById(R.id.ll_mid_container);
                if (tv_mid.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(tv_mid.getText().toString().trim())) {
                        ToastUtils.showToast(context, "请选择" + item.leftText);
                        return false;
                    }
                } else {
                    if (ll_mid_container.getChildCount() == 0) {
                        ToastUtils.showToast(context, "请选择" + item.leftText);
                        return false;
                    }
                }
            }
        }
        return true;

    }
}