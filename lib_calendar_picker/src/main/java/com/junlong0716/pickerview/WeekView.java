package com.junlong0716.pickerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeekView extends View {
    private int mDaysInWeek = 7;//一周七天
    private TextPaint mWeekPaint;//文字画笔
    private int mTextWidth = 0;//文字宽度
    private Context mContext;
    private int mTextHeight = 0;//文字高度
    private List<String> mWeekContent;//一周七天文字标题


    public WeekView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mTextHeight = ConvertUtil.dp2px(20f, mContext);
        mWeekPaint = new TextPaint();
        mWeekPaint.setAntiAlias(true);
        mWeekPaint.setTextSize((float) ConvertUtil.dp2px(14f, mContext));
        mWeekPaint.setTextAlign(Paint.Align.CENTER);
        mWeekPaint.setColor(Color.parseColor("#7B7B7B"));
        mWeekPaint.setStyle(Paint.Style.FILL);
        initWeekContent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widgetWith = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();//除掉左右padding的总显示长度
        mTextWidth = widgetWith / mDaysInWeek;
        int widgetHeight = mTextHeight + getPaddingTop() + getPaddingBottom();//文字高度加上padding
        int resolveHeight = resolveSize(widgetHeight, heightMeasureSpec);//获取自定义高度后的实际希望高度
        setMeasuredDimension(widthMeasureSpec, resolveHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startDrawText(canvas);
    }

    /**
     * 开始绘制文字
     *
     * @param canvas 画板
     */
    private void startDrawText(Canvas canvas) {
        //找中间线
        float halfLineHeight = (mWeekPaint.ascent() + mWeekPaint.descent()) / 2f;
        float helfHeight = mTextHeight / 2 + getPaddingTop();
        for (int i = 0; i < mDaysInWeek; i++) {
            if (mWeekContent.get(i).equals("日") || mWeekContent.get(i).equals("六")) {
                mWeekPaint.setColor(Color.parseColor("#FF3A30"));
            } else {
                mWeekPaint.setColor(Color.parseColor("#7B7B7B"));
            }
            canvas.drawText(mWeekContent.get(i), (float) (mTextWidth * i + mTextWidth / 2), helfHeight - halfLineHeight, mWeekPaint);
        }
    }


    private void initWeekContent() {
        mWeekContent = new ArrayList<>();
        mWeekContent.add("日");
        mWeekContent.add("一");
        mWeekContent.add("二");
        mWeekContent.add("三");
        mWeekContent.add("四");
        mWeekContent.add("五");
        mWeekContent.add("六");
    }

    //设置第一个字是星期几
    public void setStartWeek(int weekStart) {
        if (!isValidStartWeek(weekStart))
            weekStart = Calendar.getInstance().getFirstDayOfWeek();

        for (int i = 0; i < mDaysInWeek; i++)
            mWeekContent.set(i, mWeekContent.get((weekStart + i - 1) % mDaysInWeek));
    }

    private boolean isValidStartWeek(int weekStart) {
        return weekStart >= Calendar.SUNDAY && weekStart <= Calendar.SATURDAY;
    }
}
