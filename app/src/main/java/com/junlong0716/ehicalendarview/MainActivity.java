package com.junlong0716.ehicalendarview;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.junlong0716.pickerview.MonthView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MonthView mv = findViewById(R.id.mv);
        mv.setCalendarParams(2019, 2);
        mv.setVisibility(View.GONE);
        mv.setOnViewCheckedListener(new MonthView.OnViewCheckedListener() {
            @Override
            public void onViewCheckedListener(int mYear, int mMonth, int mSelectDay) {
                Toast.makeText(MainActivity.this, mYear + "-" + mMonth + "-" + mSelectDay, Toast.LENGTH_SHORT).show();
            }
        });

        final List<CalenderBean> calendarList = new ArrayList<>();

        for (int i = 1; i < 13; i++) {
            CalenderBean calenderBean = new CalenderBean();
            calenderBean.setGroupName("2019年" + i + "月");
            calenderBean.setYear(2019);
            if (i == 5)
                calenderBean.setSetCheckedDay(15);
            else
                calenderBean.setSetCheckedDay(0);
            calenderBean.setMonth(i);
            calendarList.add(calenderBean);
        }

        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, calendarList);
        RecyclerView rvList = findViewById(R.id.rv_list);
        rvList.addItemDecoration(new StickHeaderDecoration(this));
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnDayCheckedListener(new RecyclerViewAdapter.OnDayCheckedCallback() {
            @Override
            public void setOnDayCheckedListener(int mSelectDay, int pos) {
                for (CalenderBean calenderBean : calendarList)
                    calenderBean.setSetCheckedDay(0);
                calendarList.get(pos).setSetCheckedDay(mSelectDay);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

    }
}
