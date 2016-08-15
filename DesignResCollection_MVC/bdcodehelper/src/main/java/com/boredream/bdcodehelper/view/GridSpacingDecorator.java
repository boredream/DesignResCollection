package com.boredream.bdcodehelper.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingDecorator extends RecyclerView.ItemDecoration {

    private int space;

    public GridSpacingDecorator(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space / 2;
        outRect.right = space / 2;
        outRect.bottom = space;
        if (parent.getChildAdapterPosition(view) <= 1) {
            outRect.top = space * 2;
        }
    }
}
