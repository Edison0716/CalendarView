package com.junlong0716.pickerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.Calendar;

public class MonthView extends View {
    private Context mContext;
    //文字画笔
    private TextPaint mTextPaint;
    //文字选中颜色
    private TextPaint mTextSelectedPaint;
    //描述文字画笔
    private TextPaint mDesTextPaint;
    private Paint mSelectedPaint;
    //一星期7天
    private int mDaysInWeek = 7;
    //一个月最大行数
    private int mMaxWeeksLinesInMonth = 6;
    //一个月多少天
    private int mDaysInMonth = 0;
    //一行去掉内间距的宽
    private int mPaddedWidth = 0;
    //一行去掉内间距的高
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
    //选中的日期
    private int mSelectDay = 0;

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
        Typeface boldFont = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mTextPaint.setTypeface(boldFont);
        mTextPaint.setTextSize(ConvertUtil.dp2px(16f, mContext));
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mDesTextPaint = new TextPaint();
        mDesTextPaint.setAntiAlias(true);
        mDesTextPaint.setColor(Color.parseColor("#FF7E00"));
        mDesTextPaint.setTextSize(ConvertUtil.dp2px(12f, mContext));
        mDesTextPaint.setTextAlign(Paint.Align.CENTER);

        mSelectedPaint = new Paint();
        mSelectedPaint.setFilterBitmap(true);
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setDither(true);
        mSelectedPaint.setColor(Color.parseColor("#29B7B7"));

        mTextSelectedPaint = new TextPaint();
        mTextSelectedPaint.setColor(Color.WHITE);
        mTextSelectedPaint.setAntiAlias(true);
        mTextSelectedPaint.setTypeface(boldFont);
        mTextSelectedPaint.setTextSize(ConvertUtil.dp2px(16f, mContext));
        mTextSelectedPaint.setTextAlign(Paint.Align.CENTER);

        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
    }

    public void setCalendarParams(int year, int month) {
        if (isValidMonth(month - 1))
            mMonth = month - 1;
        mYear = year;
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mDaysInMonth = getDaysInMonth(mYear, mMonth);
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);
        mWeekStart = mCalendar.getFirstDayOfWeek();

        //判断最大行数
        if (mDayOfWeekStart == 6 && mDaysInMonth == 31)
            mMaxWeeksLinesInMonth = 6;
        else if (mDayOfWeekStart == 7 && mDaysInMonth >= 30)
            mMaxWeeksLinesInMonth = 6;
        else mMaxWeeksLinesInMonth = 5;
        requestLayout();
    }

    private int getDaysInMonth(int year, int month) {
        int days;
        if (month == Calendar.FEBRUARY) {
            if (year % 4 == 0)
                days = 29;
            else
                days = 28;
        } else if (month == Calendar.JUNE || month == Calendar.SEPTEMBER || month == Calendar.NOVEMBER || month == Calendar.APRIL)
            days = 30;
        else
            days = 31;
        return days;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //一个格子的宽度
        mCellWith = (MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight()) / mDaysInWeek;
        mCellHeight = mCellWith;
        //目标高度
        int targetHeight = mCellHeight * mMaxWeeksLinesInMonth + getPaddingTop() + getPaddingBottom() + mMonthHeight;
        //测量
        setMeasuredDimension(widthMeasureSpec, resolveSize(targetHeight, heightMeasureSpec));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) (event.getX() + 0.5f);
        int y = (int) (event.getY() + 0.5f);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSelectDay = getDaysLocation(x, y);
                Log.d("mSelectDay", mSelectDay + "");
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }

    //根据位置信息找到选中的天数
    private int getDaysLocation(int x, int y) {
        int paddedX = x - getPaddingLeft();
        if (paddedX < 0 || paddedX >= mPaddedWidth) return -1;
        int paddedY = y - getPaddingTop();
        if (paddedY < mMonthHeight || paddedY >= mPaddedHeight) return -1;
        int row = (paddedY - mMonthHeight) / mCellHeight; //行高 点击位置所在的第几行
        int col = paddedX * mDaysInWeek / mPaddedWidth; //列宽 点击位置所在的第几列
        int index = col + row * mDaysInWeek;//点击在第几个格子 一行7个数字 行数 * 7 + 列数 = 日期天
        int selectDay = index + 1 - getDayOffset();//这里应该去掉之前的空格
        Log.d("selectedDay", selectDay + "");
        if (isValidDay(selectDay)) return selectDay;
        else return -1;
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
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, mYear);
            calendar.set(Calendar.MONTH, mMonth);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            //高亮
            if (mSelectDay == day)
                drawSelectedTextLightBg(canvas, (float) colCenter, rowHeightCenter);

            if (mSelectDay == day){
                drawSelectedText(canvas, String.valueOf(day), (float) colCenter, rowHeightCenter - halfTextLineHeight - ConvertUtil.dp2px(8, mContext));
                drawSelectedDesText(canvas, "￥155", (float) colCenter, rowHeightCenter + halfTextLineHeight + ConvertUtil.dp2px(20, mContext));
            }
            else{
                drawDesText(canvas, "￥155", (float) colCenter, rowHeightCenter + halfTextLineHeight + ConvertUtil.dp2px(20, mContext));
                drawText(canvas, String.valueOf(day), (float) colCenter, rowHeightCenter - halfTextLineHeight - ConvertUtil.dp2px(8, mContext));
            }

            //绘制到了最后一个格子 则从头开始
            if (++colOffset == mDaysInWeek) {
                colOffset = 0;
                rowHeightCenter += rowHeight;
            }
        }
    }


    private void drawSelectedDesText(Canvas canvas, String des, float x, float y) {
        mDesTextPaint.setColor(Color.WHITE);
        canvas.drawText(des, x, y, mDesTextPaint);
    }

    private void drawSelectedText(Canvas canvas, String dayString, float x, float y) {
        canvas.drawText(dayString, x, y, mTextSelectedPaint);
    }

    private void drawSelectedTextLightBg(Canvas canvas, float x, float y) {
        canvas.drawRoundRect(new RectF(x - 60, y - 60, x + 60, y + 60), 8, 8, mSelectedPaint);
    }

    private void drawDesText(Canvas canvas, String des, float x, float y) {
        mDesTextPaint.setColor(Color.parseColor("#FF7E00"));
        canvas.drawText(des, x, y, mDesTextPaint);
    }

    private void drawText(Canvas canvas, String dayString, float x, float y) {
        //todo 判断昨天 变灰
        canvas.drawText(dayString, x, y, mTextPaint);
    }

    private boolean isValidMonth(int month) {
        return month >= Calendar.JANUARY && month <= Calendar.DECEMBER;
    }

    private boolean isValidDay(int day) {
        return day >= 1 && day <= 31;
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
