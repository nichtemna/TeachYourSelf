package com.qarea.mlfw.activity;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.widget.ListView;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.R;
import com.qarea.mlfw.adapter.WeekMenuAdapter;
import com.qarea.mlfw.util.SelectedDictionary;
import com.qarea.mlfw.util.WeekProgress;

public class WeekMenuActivity extends BaseActivity {

	// private final int LAST_WEEK = -1;
	private final int THIS_WEEK = 0;
	private final int NEXT_WEEK = 1;
	private WeekProgress[] weekList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week_menu);

		/*
		 * setData(LAST_WEEK, R.id.btn_last_week, R.id.pbar_last_week);
		 * setData(THIS_WEEK, R.id.btn_this_week, R.id.pbar_this_week);
		 * setData(NEXT_WEEK, R.id.btn_next_week, R.id.pbar_next_week);
		 */

	}

	private WeekProgress getData(int addWeek, String weekName) {

		changeWeek(addWeek);
		StringBuilder resultString = new StringBuilder();
		long dateFrom = calendar.getTimeInMillis();
		resultString.append(weekName).append("  (")
				.append(dateFormat.format(calendar.getTime())).append(" - ");
		// incorrect line
		calendar.add(Calendar.WEEK_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		resultString.append(dateFormat.format(calendar.getTime())).append(")");
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		long dateTo = calendar.getTimeInMillis();
		int totalProgress = dataProvider.getStatisticCountForPeriod(
				SelectedDictionary.getDictionaryID(), dateFrom, dateTo, false);
		int correctProgress = dataProvider.getStatisticCountForPeriod(
				SelectedDictionary.getDictionaryID(), dateFrom, dateTo, true);
		return new WeekProgress(resultString.toString(), totalProgress,
				correctProgress, dateFrom);
	}

	private void changeWeek(int addWeek) {
		calendar.setTime(new Date());
		// calendar.set(Calendar.DAY_OF_WEEK, 1);
		// calendar.clear(Calendar.DAY_OF_WEEK_IN_MONTH);
		clearCalendar();

		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

		calendar.add(Calendar.WEEK_OF_MONTH, addWeek);
		// incorrect line
		// calendar.add(Calendar.WEEK_OF_MONTH, -1);

	}

	@Override
	protected void onResume() {
		weekList = new WeekProgress[2];
		// weekList[0] = getData(LAST_WEEK, "Last week");
		weekList[0] = getData(THIS_WEEK, "This week");
		weekList[1] = getData(NEXT_WEEK, "Next week");

		WeekMenuAdapter adapter = new WeekMenuAdapter(this, weekList);
		((ListView) findViewById(android.R.id.list)).setAdapter(adapter);
		super.onResume();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater goUp = getMenuInflater();
	// goUp.inflate(R.menu.user_menu, menu);
	// return true;
	// }
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.settings:
	// Intent i = new Intent(this, SettingsActivity.class);
	// startActivity(i);
	// break;
	// }
	// return false;
	// }

}
