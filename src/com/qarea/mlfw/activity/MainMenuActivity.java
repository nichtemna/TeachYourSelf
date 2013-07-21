package com.qarea.mlfw.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.R;
import com.qarea.mlfw.util.SelectedDictionary;
import com.qarea.mlfw.viewpager.ViewPagerActivity;

public class MainMenuActivity extends BaseActivity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.week_btn:
			if (SelectedDictionary.getDictionaryID() == -1)
				Toast.makeText(this, "Select dictionary", Toast.LENGTH_SHORT)
						.show();
			else {
				calendar.setTime(new Date());
				clearCalendar();
				calendar.setFirstDayOfWeek(Calendar.MONDAY);
				calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
				calendar.add(Calendar.WEEK_OF_MONTH, 0);
				Intent intent = new Intent(this, WeekViewActivity.class);
				intent.putExtra(DBSchedule.DATE, calendar.getTimeInMillis());
				this.startActivity(intent);
				/*
				 * Intent weekIntent = new Intent(this, WeekMenuActivity.class);
				 * startActivity(weekIntent);
				 */
			}
			break;
		case R.id.bChekingKnowledge:
			if (SelectedDictionary.getDictionaryID() == -1)
				Toast.makeText(this, "Select dictionary", Toast.LENGTH_SHORT)
						.show();
			else {
				calendar.setTime(new Date());
				clearCalendar();
				Long fromDate = calendar.getTimeInMillis();
				Long toDate = fromDate + MILLSINDAY;
				// check if there is any words in any dictionary
				if (dataProvider.hasAnyWords(fromDate, toDate)) {
					final Class<? extends Activity> cls = getCheckKnowledgeActivity();
					Intent checkingKnowledgeIntent = new Intent(this, cls);
					startActivity(checkingKnowledgeIntent);
				} else {
					Toast.makeText(this, "No words in any dictionary.",
							Toast.LENGTH_LONG).show();
				}
			}

			break;
		case R.id.statistic_btn:
			if (SelectedDictionary.getDictionaryID() == -1)
				Toast.makeText(this, "Select dictionary", Toast.LENGTH_SHORT)
						.show();
			else {
				Intent checkingKnowladgeIntent = new Intent(this,
						StatisticsActivity.class);
				startActivity(checkingKnowladgeIntent);
			}

			break;
		case R.id.current_task_btn:
			if (SelectedDictionary.getDictionaryID() == -1)
				Toast.makeText(this, "Select dictionary", Toast.LENGTH_SHORT)
						.show();
			else {
				Intent intent = new Intent(this, ViewPagerActivity.class);
				calendar.setTime(new Date());
				clearCalendar();

				intent.putExtra(DBSchedule.DATE, calendar.getTimeInMillis());
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem settings = menu.add(R.string.menu_settings).setIcon(
				R.drawable.ic_settings);
		settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent i = new Intent(MainMenuActivity.this,
						SettingsActivity.class);
				startActivity(i);
				return false;
			}
		});
		MenuItem translator = menu.add(R.string.menu_translator).setIcon(
				R.drawable.ic_translate);
		translator.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (SelectedDictionary.getDictionaryID() == -1)
					Toast.makeText(MainMenuActivity.this, "Select dictionary",
							Toast.LENGTH_SHORT).show();
				else {
					Intent translator = new Intent(MainMenuActivity.this,
							TranslatorActivity.class);
					startActivity(translator);
				}
				return false;
			}
		});
		MenuItem info = menu.add(R.string.menu_info).setIcon(R.drawable.ic_inf);
		info.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent i = new Intent(MainMenuActivity.this, InfoActivity.class);
				startActivity(i);
				return false;
			}
		});

		return true;
	}

	
}
