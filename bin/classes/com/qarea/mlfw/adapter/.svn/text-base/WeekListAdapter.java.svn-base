package com.qarea.mlfw.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.R;
import com.qarea.mlfw.util.LocalDataProvider;
import com.qarea.mlfw.viewpager.ViewPagerActivity;

public class WeekListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;
    private final Calendar calendar;
    private LocalDataProvider dataProvider;
    private LayoutInflater inflater;
    private SimpleDateFormat dateFormat;
    private static final String PATTERN = "dd";
    private static final long ONE_DAY = 86400000;
    private final Calendar real_calendar;
    private Context mContext;

    public WeekListAdapter(Context context, String[] values, Calendar calendar,
                    LocalDataProvider dataProvider) {
        super(context, R.layout.raw_week_view, values);
        mContext = context;
        dateFormat = new SimpleDateFormat(PATTERN);// day in the month
        this.calendar = calendar;
        this.context = context;
        this.values = values;
        this.dataProvider = dataProvider;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        real_calendar = Calendar.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout rawLayout = (LinearLayout) inflater.inflate(R.layout.raw_week_view, parent,
                        false);
        int count = dataProvider.getCountOfWordsFromDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_WEEK, 1);

        String real_date = dateFormat.format(real_calendar.getTime());
        String app_date = values[position].substring(values[position].indexOf("\n") + 1,
                        values[position].indexOf("\n") + 3);
        if (app_date.equals(real_date)) {
            rawLayout.setBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
        }
        if (compareDates(values[position])) {
            TextView tvDate = (TextView) rawLayout.findViewById(R.id.raw_day_name);
            tvDate.setTypeface(null, Typeface.BOLD);
        }

        rawLayout.setOnClickListener(new OnItemClickListener(calendar.getTimeInMillis() - ONE_DAY));

        TextView dayName = (TextView) rawLayout.findViewById(R.id.raw_day_name);
        TextView wordCount = (TextView) rawLayout.findViewById(R.id.raw_count_word);
        dayName.setText(values[position]);

        if (count == 1) {
            wordCount.setText(count + " word");
        } else {
            wordCount.setText(count + " words");
        }

        return rawLayout;
    }

    /*
     * Return true if app_date >= real_date
     */
    private boolean compareDates(String app_date) {
        int real_day = real_calendar.get(Calendar.DAY_OF_MONTH);
        int real_month = real_calendar.get(Calendar.MONTH) + 1;
        int app_day = Integer.valueOf(app_date.substring(app_date.indexOf("\n") + 1,
                        app_date.indexOf("\n") + 3));
        int app_month = Integer.valueOf(app_date.substring(app_date.indexOf(".") + 1));
        // Log.d("TAG", " real_day " + real_day + "real_month " + real_month +
        // " app_day " + app_day
        // + " app_month " + app_month);
        if (real_month > app_month) {
            return false;
        } else {
            if (real_day > app_day) {
                return false;
            } else {
                return true;
            }
        }
    }

    private class OnItemClickListener implements OnClickListener {
        private long week;

        OnItemClickListener(long week) {
            this.week = week;
        }

        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(week);
            Intent intent = new Intent(context, ViewPagerActivity/* DayViewActivity */.class);
            intent.putExtra(DBSchedule.DATE, week);
            context.startActivity(intent);
            v.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
        }
    }

}
