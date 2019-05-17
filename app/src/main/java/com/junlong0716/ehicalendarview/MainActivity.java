package com.junlong0716.ehicalendarview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MonthView mv = findViewById(R.id.mv);
//        mv.setCalendarParams(2019, 5);
//        mv.setOnViewCheckedListener(new MonthView.OnViewCheckedListener() {
//            @Override
//            public void onViewCheckedListener(int mYear, int mMonth, int mSelectDay) {
//                Toast.makeText(MainActivity.this,mYear + "-" + mMonth + "-" + mSelectDay,Toast.LENGTH_SHORT).show();
//            }
//        });

        List<CalenderBean> calendarList = new ArrayList<>();

        for (int i = 1; i < 13; i++) {
            CalenderBean calenderBean = new CalenderBean();
            calenderBean.setGroupName("2019年" + i +"月");
            calenderBean.setYear(2019);
            calenderBean.setMonth(i);
            calendarList.add(calenderBean);
        }

        RecyclerView rvList = findViewById(R.id.rv_list);
        rvList.addItemDecoration(new StickHeaderDecoration(this));
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(new RecyclerViewAdapter(this, calendarList));
    }
}
