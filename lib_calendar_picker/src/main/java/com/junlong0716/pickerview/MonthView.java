package com.junlong0716.pickerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.List;


/**
 * FileName: MonthView
 * Author:   EdisonLi的Windows
 * Date:     2019/5/15 16:45
 * Description:
 */
public class MonthView extends View {
    private Context mContext;
    //文字画笔
    private TextPaint mTextPaint;
    //文字选中画笔
    private TextPaint mTextSelectedPaint;
    //描述文字画笔
    private TextPaint mDesTextPaint;
    //选中的高亮画笔
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
    //一号是从星期几开始滴
    private int mDayOfWeekStart = 0;
    //起始时间
    private int mWeekStart = 0;
    private int mYear = 0;
    private int mMonth = 0;
    private Calendar mCalendar = Calendar.getInstance();
    //选中的日期
    private int mSelectDay = 0;
    private OnViewCheckedListener mOnViewCheckedListener;
    //单个日期的信息
    private SparseArray<DayBaseEntity> mDaysInfo;
    //日期正常显示的颜色值
    private int mCalendarDayCommonTextColor;
    //日期描述正常显示颜色值
    private int mCalendarDesCommonTextColor;
    //日期不可用的颜色值
    private int mCalendarDayDisableTextColor;
    //日期选中的背景颜色值
    private int mCalendarDayCheckedLightColor;
    //日期选中的文字与描述字体颜色
    private int mCalendarDayCheckedTextColor;
    private int mCalendarDesCheckedTextColor;
    //选中的背景圆角
    private int mCalendarDayCheckedLightRadius;
    //日期文字大小
    private int mCalendarDayTextSize;
    //描述文字大小
    private int mCalendarDesTextSize;
    //记录一下之前选中的那一天
    private int mCheckedDay = 0;
    //记录第一次按下的距离Y
    private float mDownY = 0;

    public MonthView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null) return;
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MonthView);
        mCalendarDayCommonTextColor = typedArray.getColor(R.styleable.MonthView_calendarDayCommonTextColor, getResources().getColor(R.color.calendarDayCommonTextColor));
        mCalendarDesCommonTextColor = typedArray.getColor(R.styleable.MonthView_calendarDesCommonTextColor, getResources().getColor(R.color.calendarDesCommonTextColor));
        mCalendarDayDisableTextColor = typedArray.getColor(R.styleable.MonthView_calendarDayDisableTextColor, getResources().getColor(R.color.calendarDayDisableTextColor));
        mCalendarDayCheckedLightColor = typedArray.getColor(R.styleable.MonthView_calendarDayCheckedLightColor, getResources().getColor(R.color.calendarDayCheckedLightColor));
        mCalendarDayCheckedTextColor = typedArray.getColor(R.styleable.MonthView_calendarDayCheckedTextColor, getResources().getColor(R.color.calendarDayCheckedTextColor));
        mCalendarDesCheckedTextColor = typedArray.getColor(R.styleable.MonthView_calendarDesCheckedTextColor, getResources().getColor(R.color.calendarDayCheckedTextColor));
        mCalendarDayCheckedLightRadius = (int) typedArray.getDimension(R.styleable.MonthView_calendarDayCheckedLightRadius, 0);
        mCalendarDayTextSize = (int) typedArray.getDimension(R.styleable.MonthView_calendarDayTextSize, ConvertUtil.dp2px(mContext, 14));
        mCalendarDesTextSize = (int) typedArray.getDimension(R.styleable.MonthView_calendarDesTextSize, ConvertUtil.dp2px(mContext, 12));
        typedArray.recycle();
    }

    private void init() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mCalendarDayCommonTextColor);
        Typeface boldFont = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mTextPaint.setTypeface(boldFont);
        mTextPaint.setTextSize(mCalendarDayTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mDesTextPaint = new TextPaint();
        mDesTextPaint.setAntiAlias(true);
        mDesTextPaint.setColor(mCalendarDesCommonTextColor);
        mDesTextPaint.setTextSize(mCalendarDesTextSize);
        mDesTextPaint.setTextAlign(Paint.Align.CENTER);

        mSelectedPaint = new Paint();
        mSelectedPaint.setFilterBitmap(true);
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setDither(true);
        mSelectedPaint.setColor(mCalendarDayCheckedLightColor);

        mTextSelectedPaint = new TextPaint();
        mTextSelectedPaint.setColor(mCalendarDayCheckedTextColor);
        mTextSelectedPaint.setAntiAlias(true);
        mTextSelectedPaint.setTypeface(boldFont);
        mTextSelectedPaint.setTextSize(mCalendarDayTextSize);
        mTextSelectedPaint.setTextAlign(Paint.Align.CENTER);

        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
    }

    /**
     * @param year     显示的年份
     * @param month    显示的月份
     * @param daysInfo 与日期其对应的日期包含的信息载体
     */
    public void setCalendarParams(int year, int month, List<? extends DayBaseEntity> daysInfo) {
        mDaysInfo = new SparseArray<>();
        for (DayBaseEntity day : daysInfo)
            mDaysInfo.append(day.getDay(), day);
        if (isValidMonth(month - 1))
            mMonth = month - 1;
        mYear = year;
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.DAY_OF_MONTH, 0);
        mDaysInMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
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

    public void setCheckedDay(int day) {
        mSelectDay = day;
        mCheckedDay = day;
        invalidate();
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
        int targetHeight = mCellHeight * mMaxWeeksLinesInMonth + getPaddingTop() + getPaddingBottom();
        //测量
        setMeasuredDimension(widthMeasureSpec, resolveSize(targetHeight, heightMeasureSpec));
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) (event.getX() + 0.5f);
        int y = (int) (event.getY() + 0.5f);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                mSelectDay = getDaysLocation(x, y);
                mDownY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                if (mDaysInfo.get(mSelectDay) == null || mDaysInfo.get(mSelectDay).isDisable()) {
                    mSelectDay = mCheckedDay;
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownY - event.getY()) > 10) {
                    mSelectDay = mCheckedDay;
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                if (mSelectDay != 0)
                    mOnViewCheckedListener.onViewCheckedListener(mYear, mMonth + 1, mSelectDay, mDaysInfo.get(mSelectDay));
                break;
        }
        return true;
    }

    //根据位置信息找到选中的天数
    private int getDaysLocation(int x, int y) {
        int paddedX = x - getPaddingLeft(); //点击坐标距离控件左边的距离
        if (paddedX < 0 || paddedX >= mPaddedWidth) return -1;
        int paddedY = y - getPaddingTop();
        if (paddedY < 0 || paddedY >= mPaddedHeight) return -1;
        int row = paddedY / mCellHeight; //行高 点击位置所在的第几行
        int col = paddedX * mDaysInWeek / mPaddedWidth; //列宽 点击位置所在的第几列
        int index = col + row * mDaysInWeek;//点击在第几个格子 一行7个数字 第几行 * 7 + 第几列 = 日期天
        int selectDay = index + 1 - getDayOffset();//这里应该去掉之前的空格
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
        int rowHeightCenter = rowHeight / 2;
        float halfTextLineHeight = (mTextPaint.descent() + mTextPaint.ascent()) / 2f;

        for (int day = 1; day <= mDaysInMonth; day++) {
            //若没有对应的day信息则默认是不可用的
            boolean isDisable = true;
            String des;
            if (mDaysInfo.get(day) == null)
                des = "";
            else {
                isDisable = mDaysInfo.get(day).isDisable();
                des = mDaysInfo.get(day).getDes();
            }

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
                drawSelectedTextLightBg(canvas, (float) colCenter, rowHeightCenter, isDisable);

            if (mSelectDay == day) {
                drawSelectedText(canvas, String.valueOf(day), (float) colCenter, rowHeightCenter - halfTextLineHeight - ConvertUtil.dp2px(mContext, 8));
                drawSelectedDesText(canvas, des, (float) colCenter, rowHeightCenter + halfTextLineHeight + ConvertUtil.dp2px(mContext, 20));
            } else {
                drawDesText(canvas, des, (float) colCenter, rowHeightCenter + halfTextLineHeight + ConvertUtil.dp2px(mContext, 20), isDisable);
                drawText(canvas, day, (float) colCenter, rowHeightCenter - halfTextLineHeight - ConvertUtil.dp2px(mContext, 8), isDisable);
            }

            //绘制到了最后一个格子 则从头开始
            if (++colOffset == mDaysInWeek) {
                colOffset = 0;
                rowHeightCenter += rowHeight;
            }
        }
    }

    private void drawSelectedDesText(Canvas canvas, String des, float x, float y) {
        mDesTextPaint.setColor(mCalendarDesCheckedTextColor);
        canvas.drawText(des, x, y, mDesTextPaint);
    }

    private void drawSelectedText(Canvas canvas, String dayString, float x, float y) {
        canvas.drawText(dayString, x, y, mTextSelectedPaint);
    }

    private void drawSelectedTextLightBg(Canvas canvas, float x, float y, boolean isDisable) {
        if (isDisable) return;
        canvas.drawRoundRect(new RectF(x - mCellWith / 2f + 10, y - mCellHeight / 2f + 10, x + mCellWith / 2f - 10, y + mCellHeight / 2f - 10), mCalendarDayCheckedLightRadius, mCalendarDayCheckedLightRadius, mSelectedPaint);
    }

    private void drawDesText(Canvas canvas, String des, float x, float y, boolean isDisable) {
        if (isDisable) {
            canvas.drawText("", x, y, mDesTextPaint);
        } else {
            mDesTextPaint.setColor(mCalendarDesCommonTextColor);
            canvas.drawText(des, x, y, mDesTextPaint);
        }
    }

    private void drawText(Canvas canvas, int day, float x, float y, boolean isDisable) {
        if (isDisable) {
            mTextPaint.setColor(mCalendarDayDisableTextColor);
            canvas.drawText(day + "", x, y, mTextPaint);
        } else {
            mTextPaint.setColor(mCalendarDayCommonTextColor);
            canvas.drawText(day + "", x, y, mTextPaint);
        }
    }

    private boolean isValidMonth(int month) {
        return month >= Calendar.JANUARY && month <= Calendar.DECEMBER;
    }

    private boolean isValidDay(int day) {
        return day >= 1 && day <= mDaysInMonth;
    }

    //计算格子的偏移量
    private int getDayOffset() {
        int offset = mDayOfWeekStart - mWeekStart;
        if (mDayOfWeekStart < mWeekStart) {
            return offset + mDaysInWeek;
        } else {
            return offset;
        }
    }

    public void setOnViewCheckedListener(OnViewCheckedListener onViewCheckedListener) {
        this.mOnViewCheckedListener = onViewCheckedListener;
    }

    public interface OnViewCheckedListener {
        void onViewCheckedListener(int mYear, int mMonth, int mSelectDay, DayBaseEntity dayBaseEntity);
    }
}
