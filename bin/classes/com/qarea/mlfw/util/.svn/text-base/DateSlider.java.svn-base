package com.qarea.mlfw.util;

import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.qarea.mlfw.R;

public class DateSlider extends Dialog {

	private Calendar mTime;
	private LinearLayout mLayout;
	private Context context;
	private DatePicker fromDatePicker, toDatePicker;
	private Button doneBtn, cancelBtn;
	private Period period;

	public DateSlider(Context context, Calendar calendar, Period period) {
		super(context);
		this.context = context;
		this.period = period;
		mTime = Calendar.getInstance(Locale.getDefault());
		mTime.setTimeInMillis(calendar.getTimeInMillis());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		doneBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mTime.clear();
				mTime.set(Calendar.YEAR, fromDatePicker.getYear());
				mTime.set(Calendar.MONTH, fromDatePicker.getMonth());
				mTime.set(Calendar.DAY_OF_MONTH, fromDatePicker.getDayOfMonth());
				period.setFromDate(mTime.getTimeInMillis());
				mTime.clear();
				mTime.set(Calendar.YEAR, toDatePicker.getYear());
				mTime.set(Calendar.MONTH, toDatePicker.getMonth());
				mTime.set(Calendar.DAY_OF_MONTH, toDatePicker.getDayOfMonth());
				period.setToDate(mTime.getTimeInMillis());
				if (period.getFromDate() > period.getToDate()) {
					Toast.makeText(context,
							"Last statistic day id earlie that first",
							Toast.LENGTH_SHORT).show();
					return;
				}
				dismiss();

			}
		});
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}

	private void init() {
		setContentView(R.layout.dialog_date);
		setTitle("Period setting");
		mLayout = (LinearLayout) findViewById(R.id.dateSlider);
		LayoutParams lParam = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mLayout.setScrollContainer(true);
		TextView fromDateView = new TextView(context);
		fromDateView.setText("From date:");
		fromDateView.setTextColor(Color.WHITE);
		fromDateView.setTextSize(18f);
		fromDateView.setPadding(10, 0, 0, 0);
		mLayout.addView(fromDateView, lParam);
		fromDatePicker = new DatePicker(context);
		mLayout.addView(fromDatePicker, lParam);
		TextView toDateView = new TextView(context);
		toDateView.setTextColor(Color.WHITE);
		toDateView.setTextSize(18f);
		toDateView.setText("To date");
		toDateView.setPadding(10, 0, 0, 0);
		mLayout.addView(toDateView, lParam);
		toDatePicker = new DatePicker(context);
		mLayout.addView(toDatePicker, lParam);

		LinearLayout bLayout = new LinearLayout(context);
		bLayout.setOrientation(LinearLayout.HORIZONTAL);
		bLayout.setPadding(0, 10, 0, 0);
		cancelBtn = new Button(context);
		cancelBtn.setText("Cancel");
		LayoutParams bParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				1f);
		bLayout.addView(cancelBtn, bParams);

		doneBtn = new Button(context);
		doneBtn.setText("Done");
		bLayout.addView(doneBtn, bParams);

		mLayout.addView(bLayout, lParam);
	}

}
