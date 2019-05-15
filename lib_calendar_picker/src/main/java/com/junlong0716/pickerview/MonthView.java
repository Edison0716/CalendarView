package com.junlong0716.pickerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class MonthView extends View {
    private Context mContext;
    //文字画笔
    private TextPaint mTextPaint;
    //一星期7天
    private int mDaysInWeek = 7;
    //一个月最大行数
    private int mMaxWeeksInMonth = 6;
    //一个月多少天
    private int mDaysInMonth = 0;
    //列间距
    private int mPaddedWidth = 0;
    //行间距
    private int mPaddedHeight = 0;
    //一个格子的宽高
    private int mCellWith;
    private int mCellHeight;
    //
    private int mDayOfWeekStart = 0;
    //起始时间
    private int mWeekStart = 0;
    private int mMonthHeight = 0;
    private int mYear = 0;
    private int mMonth = 0;
    private Calendar mCalendar = Calendar.getInstance();

    public MonthView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#333333"));
        mTextPaint.setTextSize(ConvertUtil.dp2px(15f, mContext));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
    }

    public void setCalendarParams(int year, int month, int weekStart) {
        if (isValidMonth(month - 1))
            mMonth = month - 1;
        mYear = year;
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mDaysInMonth = getDaysInMonth(year, month);
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);
        if (isValidDay(weekStart))
            mWeekStart = weekStart;
        else
            mWeekStart = mCalendar.getFirstDayOfWeek();
        requestLayout();
    }

    private int getDaysInMonth(int year, int month) {
        int days = 0;

        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.DECEMBER:
            case Calendar.OCTOBER:
                days = 31;
                break;
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
            case Calendar.APRIL:
                days = 30;
                break;
            case Calendar.FEBRUARY:
                if (year % 4 == 0)
                    days = 29;
                else
                    days = 28;
                break;
        }
        return days;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //一个格子的宽度
        mCellWith = (MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight()) / mDaysInWeek;
        mCellHeight = mCellWith;
        //目标高度
        int targetHeight = mCellHeight * mMaxWeeksInMonth + getPaddingTop() + getPaddingBottom() + mMonthHeight;
        //测量
        setMeasuredDimension(widthMeasureSpec, resolveSize(targetHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //摆放之后的宽高
        int width = right - left;
        int height = bottom - top;

        int paddingRight = width - getPaddingRight();
        int paddingBottom = height - getPaddingBottom();

        //除去上下左右的padding后的控件宽高
        int paddedWidth = paddingRight - getPaddingLeft();
        int paddedHeight = paddingBottom - getPaddingTop();

        if (this.mPaddedHeight == paddedHeight || this.mPaddedWidth == paddedWidth) return;

        this.mPaddedWidth = paddedWidth;
        this.mPaddedHeight = paddedHeight;

        //算格子的宽高
        mCellWith = paddedWidth / mDaysInWeek;
        mCellHeight = mCellWith;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDays(canvas);
    }

    private void drawDays(Canvas canvas) {
        //行宽
        int rowHeight = mCellHeight;
        //列宽
        int colWidth = mCellWith;
        int colOffset = getDayOffset();
        //todo monthHeight
        int rowHeightCenter = rowHeight / 2 + mMonthHeight;
        float halfTextLineHeight = (mTextPaint.descent() + mTextPaint.ascent()) / 2f;

        for (int day = 1; day <= mDaysInMonth; day++) {
            //每个月的一号并不都顶格 计算起始点
            int cellStartX = colWidth * colOffset;
            int cellStartY = rowHeightCenter - rowHeight / 2;
            //一个格子的中点横坐标
            int colCenter = cellStartX + colWidth / 2;

            //todo 点击高亮
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, mYear);
            calendar.set(Calendar.MONTH, mMonth);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            drawText(canvas, String.valueOf(day), (float) colCenter, rowHeightCenter - halfTextLineHeight,
                    calendar, mTextPaint);

            if (++colOffset == mDaysInWeek) {
                colOffset = 0;
                rowHeightCenter += rowHeight;
            }
        }
    }

    private void drawText(Canvas canvas, String dayString, float x, float y, Calendar calendar, Paint paint) {
        //todo 判断昨天 变灰
        canvas.drawText(dayString, x, y, paint);
    }

    private boolean isValidMonth(int month) {
        return month >= Calendar.JANUARY && month <= Calendar.DECEMBER;
    }

    private boolean isValidDay(int day) {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY;
    }

    //计算月份第一个格子的偏移量
    private int getDayOffset() {
        int offset = mDayOfWeekStart - mWeekStart;
        if (mDayOfWeekStart < mWeekStart) {
            return offset + mDaysInWeek;
        } else {
            return offset;
        }
    }
}
