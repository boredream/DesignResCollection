package com.boredream.bdcodehelper.adapter;


import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.entity.SettingItem;

import java.util.List;

/**
 * 设置选项列表适配器
 * <p>
 * Item通用样式为：左侧图标、中间文字、右侧文字、右侧图标。
 */
public class SettingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_SETTING_ITEM = 0x10001;

    protected List<SettingItem> datas;
    protected AdapterView.OnItemClickListener mOnItemClickListener;

    public SettingRecyclerAdapter(List<SettingItem> datas, AdapterView.OnItemClickListener listener) {
        this.datas = datas;
        mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_VIEW_TYPE_SETTING_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_left;
        public TextView tv_mid;
        public TextView tv_right;
        public ImageView iv_right;

        public ViewHolder(final View itemView) {
            super(itemView);

            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            tv_mid = (TextView) itemView.findViewById(R.id.tv_mid);
            tv_right = (TextView) itemView.findViewById(R.id.tv_right);
            iv_right = (ImageView) itemView.findViewById(R.id.iv_right);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder settingViewHolder = (ViewHolder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(null, view, position, -1);
                }
            }
        });
        SettingItem data = datas.get(position);

        settingViewHolder.iv_left.setImageResource(data.leftImgRes);
        if (!TextUtils.isEmpty(data.rightText)) {
            settingViewHolder.tv_right.setVisibility(View.VISIBLE);
            settingViewHolder.tv_right.setText(data.rightText);
        } else {
            settingViewHolder.tv_right.setVisibility(View.GONE);
        }
        settingViewHolder.tv_mid.setText(data.midText);

        if (data.rightImage != -1) {
            settingViewHolder.iv_right.setVisibility(View.VISIBLE);
            settingViewHolder.iv_right.setImageResource(data.rightImage);
        } else {
            settingViewHolder.iv_right.setVisibility(View.GONE);
        }
    }

}
