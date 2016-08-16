package com.boredream.designrescollection.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.designrescollection.R;
import com.boredream.designrescollection.base.BaseFragment;

public class UserFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_user, null);
        return view;
    }
}
