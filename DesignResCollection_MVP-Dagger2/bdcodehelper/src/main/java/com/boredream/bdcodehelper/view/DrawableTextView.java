package com.boredream.bdcodehelper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;


public class DrawableTextView extends TextView {

    private final int widthPx;
    private final int heightPx;

    public DrawableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.DrawableTextView);

        widthPx = ta.getDimensionPixelSize(
                R.styleable.DrawableTextView_drawableWidth, -1);
        heightPx = ta.getDimensionPixelSize(
                R.styleable.DrawableTextView_drawableHeight, -1);

        // 左右上下四个图片
        Drawable[] drawables = getCompoundDrawables();

        setDrawables(drawables);

        ta.recycle();
    }

    /**
     * 左上右下四个图片,只会取一个方向图片使用
     */
    public void setDrawables(Drawable[] drawables) {
        // 左右上下四个图片
        Drawable drawable = null;
        // 如果其中一个方向有图片,获取之
        for (int i = 0; i < drawables.length; i++) {
            if (drawables[i] != null) {
                drawable = drawables[i];
                break;
            }
        }

        // 设置宽高
        if (drawable != null && widthPx != -1 && heightPx != -1) {
            drawable.setBounds(0, 0, widthPx, heightPx);
        }

        // 将图片放回到TextView中
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context) {
        this(context, null);
    }

}