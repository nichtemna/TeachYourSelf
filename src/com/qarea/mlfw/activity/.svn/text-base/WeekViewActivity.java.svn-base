package com.qarea.mlfw.activity;

import java.util.Calendar;

import android.os.Bundle;
import android.widget.ListView;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.R;
import com.qarea.mlfw.adapter.WeekListAdapter;

public class WeekViewActivity extends BaseActivity {
    private ListView weekList;
    private String[] days;
    private long currentDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        currentDate = this.getIntent().getLongExtra(DBSchedule.DATE, 0);
        calendar.setTimeInMillis(currentDate);

        weekList = (ListView) findViewById(android.R.id.list);
        days = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
                        "Sunday" };
        for (int i = 0; i < days.length; i++) {
            days[i] = days[i] + "\n" + dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        calendar.setTimeInMillis(currentDate);
    }

    @Override
    protected void onResume() {

        calendar.setTimeInMillis(currentDate);
        clearCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        WeekListAdapter adapter = new WeekListAdapter(this, days, calendar, dataProvider);
        weekList.setAdapter(adapter);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(currentDate);
        super.onResume();
    }

}
