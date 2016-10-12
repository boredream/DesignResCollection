package com.boredream.bdcodehelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;

import java.util.List;

public class ListDropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ListDropDownAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_default_drop_down, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.tv_title.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
                viewHolder.tv_title.setBackgroundResource(R.color.check_bg);
            } else {
                viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.tv_title.setBackgroundResource(R.color.white);
            }
        }
    }

    static class ViewHolder {
        TextView tv_title;

        ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}