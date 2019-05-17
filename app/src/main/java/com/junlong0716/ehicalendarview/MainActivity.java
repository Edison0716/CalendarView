package com.junlong0716.ehicalendarview;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.junlong0716.pickerview.MonthView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MonthView mv = findViewById(R.id.mv);
        mv.setCalendarParams(2019, 5);
        mv.setOnViewCheckedListener(new MonthView.OnViewCheckedListener() {

            @Override
            public void onViewCheckedListener(int mYear, int mMonth, int mSelectDay) {
                Toast.makeText(MainActivity.this,mYear + "-" + mMonth + "-" + mSelectDay,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
