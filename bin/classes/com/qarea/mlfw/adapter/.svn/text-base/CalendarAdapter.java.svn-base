package com.qarea.mlfw.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.qarea.mlfw.R;
import com.qarea.mlfw.model.Word;
import com.qarea.mlfw.util.LocalDataProvider;

/*
 * This abstract adapter create clickable calendar
 * 
 * */
public abstract class CalendarAdapter extends BaseAdapter implements OnClickListener {
    public final String[] months = { "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December" };
    public final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    public final int month, year;
    public static final int DAY_OFFSET = 1;
    public int daysInMonth;// prevMonthDays;
    public final List<String> list;
    public int currentDayOfMonth;
    public int currentWeekDay;
    public Context context;
    public Button gridcell;
    public String mode;
    public ArrayList<Word> words;
    public int dictionaryId;
    public long currentDate;
    public int from;
    public int selectedDate;
    public LocalDataProvider dataProvider;

    // Days in Current Month
    public CalendarAdapter(Context context, int textViewResourceId, int month, int year,
                    ArrayList<Word> words, int dictionaryId, long currentDate) {
        super();
        this.context = context;
        this.list = new ArrayList<String>();
        this.month = month;
        this.year = year;
        this.words = words;
        this.dictionaryId = dictionaryId;
        this.currentDate = currentDate;
        this.dataProvider = LocalDataProvider.getInstance(context);
        // this.mode
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        calendar.setTimeInMillis(currentDate);
        selectedDate = calendar.get(Calendar.DAY_OF_MONTH);
        printMonth(this.month, this.year);
    }

    private void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    private void printMonth(int mm, int yy) {
        // The number of days to leave blank at
        // the start of this month.
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;

        int currentMonth = mm - 1;
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            prevYear = yy;
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
        } else {
            prevMonth = currentMonth - 1;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
        }
        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;
        if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
            ++daysInMonth;
        }

        // Trailing Month days
        for (int i = 1; i < trailingSpaces; i++) {
            list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY"
                            + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
        }

        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {
            if (i < getCurrentDayOfMonth()) {
                list.add(String.valueOf(i) + "-GREY");
            } else {
                list.add(String.valueOf(i) + "-WHITE");
            }
        }

        // Leading Month days
        for (int i = 0; i < list.size() % 7; i++) {
            list.add(String.valueOf(i + 1) + "-GREY");
        }
    }

    private String getMonthAsString(int i) {
        return months[i];
    }

    private int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
        }

        // Get a reference to the Day gridcell
        gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
        gridcell.setOnClickListener(this);

        // ACCOUNT FOR SPACING

        String[] day_color = list.get(position).split("-");
        String theday = day_color[0];

        // Set the Day GridCell
        gridcell.setText(theday);
        gridcell.setTag(list.get(position));

        if (day_color[1].equals("GREY")) {
            gridcell.setTextColor(context.getResources().getColor(R.color.gray));
        }
        if (day_color[1].equals("WHITE")) {
            gridcell.setTextColor(Color.WHITE);
        }

        return row;
    }

    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    @Override
    public void onClick(View v) {
        move(v);
    }

    // public abstract void move(View view);

    public void move(View view) {
        String s = (String) view.getTag();
        if (s.substring(s.indexOf("-") + 1, s.length()).equals("GREY")) {
            Toast.makeText(context, "Can't move to this date", Toast.LENGTH_SHORT).show();
        } else if (s.substring(s.indexOf("-") + 1, s.length()).equals("WHITE")) {
            int day = Integer.parseInt(s.substring(0, s.indexOf("-")));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(new Date());
            day = day - calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.AM_PM, 0);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DAY_OF_WEEK, day);
            changeDate(calendar.getTimeInMillis());
            ((Activity) context).finish();
        }
    }

    protected abstract void changeDate(long newDate);
}
