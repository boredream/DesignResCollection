package com.boredream.designrescollection.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdcodehelper.adapter.SettingRecyclerAdapter;
import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.net.GlideHelper;
import com.boredream.designrescollection.ui.login.LoginActivity;

import java.util.List;

/**
 * 更多选项列表适配器
 * <p>
 * 第一个位置为HEADER类型,对应用户信息<br/>
 * 其他位置为LIST类型,对应选项item
 */
public class MoreRecyclerAdapter extends SettingRecyclerAdapter {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;

    private User user;

    public MoreRecyclerAdapter(List<SettingItem> datas, OnItemClickListener listener) {
        super(datas, listener);
    }

    @Override
    public int getItemCount() {
        // header + 1
        return datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_VIEW_TYPE_HEADER : super.getItemViewType(position);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class ViewHolderUserHeader extends RecyclerView.ViewHolder {

        public View include_more_header;
        public ImageView iv_avatar;
        public TextView tv_name;
        // not login
        public View include_more_no_header;
        public Button btn_login;

        public ViewHolderUserHeader(final View itemView) {
            super(itemView);

            include_more_header = itemView.findViewById(R.id.include_more_header);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            include_more_no_header = itemView.findViewById(R.id.include_more_no_header);
            btn_login = (Button) itemView.findViewById(R.id.btn_login);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_header, parent, false);
            return new ViewHolderUserHeader(v);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_VIEW_TYPE_HEADER) {
            // header
            ViewHolderUserHeader viewHolderHeader = (ViewHolderUserHeader) holder;
            if (user != null) {
                // 如果已登录,用户数据非空,则显示用户信息
                viewHolderHeader.include_more_header.setVisibility(View.VISIBLE);
                viewHolderHeader.include_more_no_header.setVisibility(View.GONE);

                GlideHelper.showAvatar(holder.itemView.getContext(), user.getAvatar(), viewHolderHeader.iv_avatar);
                viewHolderHeader.tv_name.setText(user.getNickname());

                viewHolderHeader.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            // header使用特殊的position -1
                            mOnItemClickListener.onItemClick(null, view, -1, -1);
                        }
                    }
                });
            } else {
                // 未登录,提供登录按钮,点击进入登录页
                viewHolderHeader.include_more_header.setVisibility(View.GONE);
                viewHolderHeader.include_more_no_header.setVisibility(View.VISIBLE);

                viewHolderHeader.btn_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        view.getContext().startActivity(intent);
                    }
                });
            }
        } else {
            // setting item
            // 第一个位置多了个HEADER,所以position差1
            super.onBindViewHolder(holder, position - 1);
        }
    }

}
