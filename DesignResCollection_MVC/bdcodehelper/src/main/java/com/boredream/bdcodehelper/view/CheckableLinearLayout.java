package com.boredream.bdcodehelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * This is a simple wrapper for {@link LinearLayout} that implements the {@link Checkable}
 * interface by keeping an internal 'checked' state flag.
 * <p>
 * This can be used as the root view for a custom list item layout for
 * {@link android.widget.AbsListView} elements with a
 * {@link android.widget.AbsListView#setChoiceMode(int) choiceMode} set.
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private boolean mChecked = false;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean b) {
        if (b != mChecked) {
            mChecked = b;
            refreshDrawableState();
        }
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}