package com.qarea.mlfw;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.qarea.mlfw.activity.CheckKnowledgeSwipeActivity;
import com.qarea.mlfw.activity.CheckingKnowledgeActivity;
import com.qarea.mlfw.activity.InfoActivity;
import com.qarea.mlfw.activity.MainMenuActivity;
import com.qarea.mlfw.activity.SettingsActivity;
import com.qarea.mlfw.activity.TranslatorActivity;
import com.qarea.mlfw.util.LocalDataProvider;
import com.qarea.mlfw.util.SelectedDictionary;

public class BaseActivity extends SherlockFragmentActivity {

	protected Calendar calendar;

	protected Date date;
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final int SETTINGS_NUMBER = 0;
	public static final int MAX_REPEAT_WORD_COUNT = 7;
	public static final int MILLSINHOUR = 60 * 60 * 1000;
	public static final long MILLSINDAY = 24 * 60 * 60 * 1000;
	public static final String SAVE_WORD = "DictionaryId";
	public static final String HOURS_START = "HoursStart";
	public static final String HOURS_END = "HoursEnd";
	public static final String TIMEOUT = "timeout";
	public static final String LANGUAGE = "language";
	public static final String NOT_REMIND = "not_remind_today";
	public static final String VOICE_ENABLED = "voice_enabled";
	public static final String REPEAT_COUNT = "repeat_count";
	public static final String MOVE_WORD = "move_word";
	public static final String REPEATE_WORD = "repeate_word";
	public static final String REPEATE_WORD_MOVE = "repeate_word_move";
	public static final String REPEATE_WORD_ADD = "repeate_word_add";
	public static final String MOVE_ARRAY_WORDS = "move_array_words";
	public static final String MOVE_TIME = "move_time";
	public static final String MOVE_DICTIONARY = "move_dictionary";
	private static final String PATTERN = "dd.MM";
	private static final String PATTERN_DAY_NAME = "EEEE";
	public static final String body = "You have long not checked the knowledge!";
	public static final String title = "Checked knowledge!";
	public static final long SECONDS_IN_DAY = 3600000;

	public static int timeoutHours;
	public static int startHours;
	public static int endHours;
	public static boolean notRemind;

	protected SimpleDateFormat dateFormat;
	protected SimpleDateFormat dayFormat;
	protected LocalDataProvider dataProvider;
	protected TextToSpeech myTts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		calendar = Calendar.getInstance(Locale.getDefault());
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);

		dateFormat = new SimpleDateFormat(PATTERN, Locale.getDefault());
		dayFormat = new SimpleDateFormat(PATTERN_DAY_NAME, Locale.getDefault());
		dataProvider = LocalDataProvider.getInstance(this);
		date = new Date();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent homeIntent = new Intent(BaseActivity.this,
					MainMenuActivity.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(homeIntent);
		}
		return (true);
	}

	@Override
	protected void onDestroy() {
		dataProvider.close();
		super.onDestroy();
	}

	protected void clearCalendar() {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.AM_PM, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem setings = menu.add(R.string.menu_settings).setIcon(
				R.drawable.ic_settings);
		setings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent i = new Intent(BaseActivity.this, SettingsActivity.class);
				startActivity(i);
				return false;
			}
		});
		MenuItem settings = menu.add(R.string.menu_home).setIcon(
				R.drawable.ic_home);
		settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent homeIntent = new Intent(BaseActivity.this,
						MainMenuActivity.class);
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(homeIntent);
				return false;
			}
		});
		MenuItem translator = menu.add(R.string.menu_translator).setIcon(
				R.drawable.ic_translate);
		translator.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (SelectedDictionary.getDictionaryID() == -1)
					Toast.makeText(BaseActivity.this, "Select dictionary",
							Toast.LENGTH_SHORT).show();
				else {
					Intent translator = new Intent(BaseActivity.this,
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
				Intent info = new Intent(BaseActivity.this, InfoActivity.class);
				startActivity(info);
				return false;
			}
		});

		return true;
	}

	public static String getCurrentDate() {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		return cal.get(Calendar.YEAR) + "_" + addZero(cal.get(Calendar.MONTH))
				+ "_" + addZero(cal.get(Calendar.DAY_OF_MONTH));
	}

	private static String addZero(int number) {
		String number_zero = String.valueOf(number);
		if (number_zero.length() == 1) {
			number_zero = "0" + number_zero;
		}
		return number_zero;
	}

	public Class<? extends Activity> getCheckKnowledgeActivity() {
		final Class<? extends Activity> cls;
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		int checkMode = settings.getInt(Extras.CHECK_MODE, 1);
		if (checkMode == SettingsActivity.CHECK_KNOWLEDGE_TAP) {
			cls = CheckingKnowledgeActivity.class;
		} else {
			cls = CheckKnowledgeSwipeActivity.class;
		}
		return cls;
	}

	public boolean isWords() {
		calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTime(new Date());
		clearCalendar();
		Long fromDate = calendar.getTimeInMillis();
		Long toDate = fromDate + LocalDataProvider.MILLSINDAY;
		return dataProvider.hasAnyWords(fromDate, toDate);
	}
}
