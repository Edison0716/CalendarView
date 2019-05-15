package com.junlong0716.ehicalendarview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.junlong0716.pickerview.MonthView;
import com.junlong0716.pickerview.WeekView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MonthView mv = findViewById(R.id.mv);
        mv.setCalendarParams(2019,1, Calendar.SUNDAY);
    }
}
