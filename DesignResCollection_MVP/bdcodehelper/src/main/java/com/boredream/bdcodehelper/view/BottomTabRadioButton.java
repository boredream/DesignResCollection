package com.boredream.bdcodehelper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RadioButton;

import com.boredream.bdcodehelper.R;

public class BottomTabRadioButton extends RadioButton {

    /**
     * 提示模式 - 无提示
     */
    private static final int MODE_NONE = 0;

    /**
     * 提示模式 - 红点提示
     */
    private static final int MODE_POINT = 1;

    /**
     * 提示模式 - 数字提示
     */
    private static final int MODE_NUM = 2;

    /**
     * 默认红点默认半径
     */
    private static final float DEFAULT_POINT_RADIUS = 4; //dp

    /**
     * 默认数字圆圈半径
     */
    private static final float DEFAULT_NUM_RADIUS = 8; //dp

    /**
     * 默认数字文字大小
     */
    private static final float DEFAULT_NUM_SIZE = 10; //sp

    private Paint hintPaint;
    private Paint textPaint;
    private Rect textRect = new Rect();

    /**
     * 提示模式
     */
    private int hint_mode;

    /**
     * 圆填充色
     */
    private int hint_color;

    /**
     * 据顶部距离
     */
    private int hint_toppadding;

    /**
     * 据右侧距离
     */
    private int hint_rightpadding;

    /**
     * 圆点半径
     */
    private int point_radius;

    /**
     * 数字
     */
    private int num;

    /**
     * 数字圆圈半径
     */
    private int num_radius;

    /**
     * 数字文字大小
     */
    private int num_size;

    /**
     * 数字文字颜色
     */
    private int num_color;

    public int getHint_mode() {
        return hint_mode;
    }

    public void setHint_mode(int hint_mode) {
        this.hint_mode = hint_mode;
        invalidate();
    }

    public int getHint_color() {
        return hint_color;
    }

    public void setHint_color(int hint_color) {
        this.hint_color = hint_color;
        invalidate();
    }

    public int getHint_toppadding() {
        return hint_toppadding;
    }

    public void setHint_toppadding(int hint_toppadding) {
        this.hint_toppadding = hint_toppadding;
        invalidate();
    }

    public int getHint_rightpadding() {
        return hint_rightpadding;
    }

    public void setHint_rightpadding(int hint_rightpadding) {
        this.hint_rightpadding = hint_rightpadding;
        invalidate();
    }

    public int getPoint_radius() {
        return point_radius;
    }

    public void setPoint_radius(int point_radius) {
        this.point_radius = point_radius;
        invalidate();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        invalidate();
    }

    public int getNum_radius() {
        return num_radius;
    }

    public void setNum_radius(int num_radius) {
        this.num_radius = num_radius;
        invalidate();
    }

    public int getNum_size() {
        return num_size;
    }

    public void setNum_size(int num_size) {
        this.num_size = num_size;
        invalidate();
    }

    public int getNum_color() {
        return num_color;
    }

    public void setNum_color(int num_color) {
        this.num_color = num_color;
        invalidate();
    }

    public BottomTabRadioButton(Context context) {
        super(context);
    }

    public BottomTabRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public BottomTabRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomTabRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        Context context = getContext();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        // 根据attrs获取属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomTabRadioButton);

        hint_mode = ta.getInt(R.styleable.BottomTabRadioButton_hint_mode,
                MODE_NONE);
        hint_color = ta.getColor(R.styleable.BottomTabRadioButton_hint_color,
                Color.RED);

        hint_toppadding = ta.getDimensionPixelSize(R.styleable.BottomTabRadioButton_hint_toppadding,
                0);
        hint_rightpadding = ta.getDimensionPixelSize(R.styleable.BottomTabRadioButton_hint_rightpadding,
                0);

        float defaultPointRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_POINT_RADIUS, metrics);
        point_radius = ta.getDimensionPixelSize(R.styleable.BottomTabRadioButton_point_radius,
                (int) (defaultPointRadius + 0.5f));

        num = ta.getInt(R.styleable.BottomTabRadioButton_num,
                -1);
        float defaultNumRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_NUM_RADIUS, metrics);
        num_radius = ta.getDimensionPixelSize(R.styleable.BottomTabRadioButton_num_radius,
                (int) (defaultNumRadius + 0.5f));
        float defaultNumSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, DEFAULT_NUM_SIZE, metrics);
        num_size = ta.getDimensionPixelSize(R.styleable.BottomTabRadioButton_num_size,
                (int) (defaultNumSize + 0.5f));
        num_color = ta.getDimensionPixelSize(R.styleable.BottomTabRadioButton_num_color,
                Color.WHITE);

        ta.recycle();

        // 初始化背景圆画笔
        hintPaint = new Paint();
        hintPaint.setAntiAlias(true);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setColor(hint_color);

        // 初始化数字文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(num_color);
        textPaint.setTextSize(num_size);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (hint_mode == MODE_POINT) { // 如果是圆点模式
            // 直接画一个圆点
            float width = getWidth();
            float centerX = width - hint_rightpadding - point_radius;
            float centerY = hint_toppadding + point_radius;

            canvas.drawCircle(centerX, centerY, point_radius, hintPaint);
        } else if (hint_mode == MODE_NUM) { // 如果是数字模式
            // 1.画一个背景圆
            float width = getWidth();
            float centerX = width - hint_rightpadding - num_radius;
            float centerY = hint_toppadding + num_radius;

            canvas.drawCircle(centerX, centerY, num_radius, hintPaint);

            // 2.画数字
            if (num >= 0) {
                String numStr = String.valueOf(num);

                if (num >= 100) {
                    // 如果超过100使用"..."
                    numStr = "...";
                }
                textPaint.getTextBounds(numStr, 0, numStr.length(), textRect);
                float textCenterY = centerY + textRect.bottom - textRect.top / 2;
                canvas.drawText(numStr, centerX, textCenterY, textPaint);
            }
        }
    }
}
