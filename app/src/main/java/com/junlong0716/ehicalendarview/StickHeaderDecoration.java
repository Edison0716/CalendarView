package com.junlong0716.ehicalendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * 吸顶效果装饰器
 */
public class StickHeaderDecoration extends RecyclerView.ItemDecoration {

    //头部的高
    private int mItemHeaderHeight;

    //画笔，绘制头部和分割线
    private Paint mItemHeaderPaint;
    private Paint mItemStickHeaderPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Rect mTextRect;

    StickHeaderDecoration(Context context) {
        mItemHeaderHeight = dp2px(context);

        mTextRect = new Rect();
        mItemHeaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mItemHeaderPaint.setColor(Color.WHITE);

        mItemStickHeaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mItemStickHeaderPaint.setColor(Color.parseColor("#FFFFFF"));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(46);
        mTextPaint.setColor(Color.parseColor("#333333"));

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.GRAY);
    }


    //绘制Item的分割线和组头
    @Override
    public void onDraw(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getAdapter() instanceof RecyclerViewAdapter) {
            RecyclerViewAdapter adapter = (RecyclerViewAdapter) parent.getAdapter();
            int count = parent.getChildCount();//获取可见范围内Item的总数
            for (int i = 0; i < count; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildLayoutPosition(view);
                boolean isHeader = adapter.isItemHeader(position);
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                if (isHeader) {
                    c.drawRect(left, view.getTop() - mItemHeaderHeight, right, view.getTop(), mItemHeaderPaint);
                    mTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                    c.drawText(adapter.getGroupName(position), view.getWidth() / 2 - mTextRect.width() / 2, (view.getTop() - mItemHeaderHeight) + mItemHeaderHeight / 2f + mTextRect.height() / 2f, mTextPaint);
                } else {
                    c.drawRect(left, view.getTop() - 1, right, view.getTop(), mLinePaint);
                }
            }
        }
    }


    /**
     * 绘制Item的顶部布局（吸顶效果）
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getAdapter() instanceof RecyclerViewAdapter) {
            RecyclerViewAdapter adapter = (RecyclerViewAdapter) parent.getAdapter();
            int position = ((LinearLayoutManager) (Objects.requireNonNull(parent.getLayoutManager()))).findFirstVisibleItemPosition();
            View view = Objects.requireNonNull(parent.findViewHolderForAdapterPosition(position)).itemView;
            boolean isHeader = adapter.isItemHeader(position + 1);
            int top = parent.getPaddingTop();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            if (isHeader) {
                int bottom = Math.min(mItemHeaderHeight, view.getBottom());
                c.drawRect(left, top + view.getTop() - mItemHeaderHeight, right, top + bottom, mItemStickHeaderPaint);
                mTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                //标题文字居中
                c.drawText(adapter.getGroupName(position), view.getWidth() / 2 - mTextRect.width() / 2, top + mItemHeaderHeight / 2f + mTextRect.height() / 2f - (mItemHeaderHeight - bottom), mTextPaint);
            } else {
                c.drawRect(left, top, right, top + mItemHeaderHeight, mItemStickHeaderPaint);
                mTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                c.drawText(adapter.getGroupName(position), view.getWidth() / 2 - mTextRect.width() / 2, top + mItemHeaderHeight / 2f + mTextRect.height() / 2f, mTextPaint);
            }
            c.save();
        }

    }

    /**
     * 设置Item的间距
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent,@NonNull RecyclerView.State state) {
        if (parent.getAdapter() instanceof RecyclerViewAdapter) {
            RecyclerViewAdapter adapter = (RecyclerViewAdapter) parent.getAdapter();
            int position = parent.getChildLayoutPosition(view);
            boolean isHeader = adapter.isItemHeader(position);
            if (isHeader) {
                outRect.top = mItemHeaderHeight;
            } else {
                outRect.top = 1;
            }
        }
    }


    /**
     * dp转换成px
     */
    private int dp2px(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) 40 * scale + 0.5f);
    }
}
