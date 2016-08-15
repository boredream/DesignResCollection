package com.boredream.bdcodehelper.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class EmptyItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * decoration添加的位置,为空时所有item都添加
     */
    private List<Integer> dividerPositionList;
    private int height;

    public EmptyItemDecoration(int height) {
        this.height = height;
    }

    public EmptyItemDecoration(Integer[] dividerPositions, int height) {
        this.dividerPositionList = Arrays.asList(dividerPositions);
        this.height = height;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (dividerPositionList == null || dividerPositionList.contains(position)) {
            outRect.set(0, 0, 0, height);
        }
    }
}
