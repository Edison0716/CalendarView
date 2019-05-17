package com.junlong0716.ehicalendarview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junlong0716.pickerview.MonthView;

import java.util.List;

/**
 * 分组列表适配器
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<CalenderBean> mList;


    RecyclerViewAdapter(Context context, List<CalenderBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewAdapter.ViewHolder viewHolder;
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_month_view, parent, false);
        viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mv.setCalendarParams(mList.get(position).getYear(), mList.get(position).getMonth());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    /**
     * 判断position对应的Item是否是组的第一项
     *
     * @param position 位置
     * @return 是否是第一项
     */
    boolean isItemHeader(int position) {
        if (position == 0) {
            return true;
        } else {
            String lastGroupName = mList.get(position - 1).getGroupName();
            String currentGroupName = mList.get(position).getGroupName();
            //判断上一个数据的组别和下一个数据的组别是否一致，如果不一致则是不同组，也就是为第一项（头部）
            return !lastGroupName.equals(currentGroupName);
        }
    }

    //获取组名
    String getGroupName(int position) {
        return mList.get(position).getGroupName();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        MonthView mv;

        ViewHolder(View itemView) {
            super(itemView);
            mv = itemView.findViewById(R.id.mv);
        }
    }
}
