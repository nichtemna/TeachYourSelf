package com.qarea.mlfw.viewpager;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.activity.MainMenuActivity;
import com.qarea.mlfw.activity.SettingsActivity;
import com.qarea.mlfw.activity.TranslatorActivity;
import com.qarea.mlfw.fragment.DayViewFragment;

public class ViewPagerActivity extends SherlockFragmentActivity implements
		OnInitListener, OnMenuItemClickListener {
	public static final int TAG_FOR_CALENDAR = 1;
	private static final long ONE_DAY = 86400000;
	public static final String DELETE = "delete";
	public static final String MOVE = "move";

	private MyAdapter mAdapter;
	private ViewPager mPager;
	private static long currentDate;
	private static int viewDay;
	private Date d;
	public TextToSpeech myTTS;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager_all_days);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);

		initialize();
		setVoice();
	}

	private void initialize() {
		currentDate = this.getIntent().getLongExtra(DBSchedule.DATE, 0);

		d = new Date();
		d.setTime(currentDate);
		viewDay = d.getDay() - 1;
		if (viewDay == -1) {
			viewDay = 6;
		}
		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mPager.setCurrentItem(viewDay);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {
				viewDay = arg0;
			}
		});
	}

	// voice installing
	private void setVoice() {
		// Intent checkTTSIntent = new Intent();
		// checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		// startActivityForResult(checkTTSIntent, Extras.VOICE_CHECK_CODE);
		myTTS = new TextToSpeech(this, this);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Extras.VOICE_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				myTTS = new TextToSpeech(this, this);
			} else {
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}
	}

	// init text to speach
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			myTTS.setLanguage(Locale.ENGLISH);
		} else if (status == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
		if (myTTS.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE) {
			myTTS.setLanguage(Locale.ENGLISH);
		}
	}

	// adapter for days in week
	public static class MyAdapter extends FragmentPagerAdapter {
		Date d;
		long monday, tusday, wednesday, thersday, friday, saturday, sunday;

		DayViewFragment dayMonday;
		DayViewFragment dayTusday;
		DayViewFragment dayWednesday;
		DayViewFragment dayThersday;
		DayViewFragment dayFriday;
		DayViewFragment daySaturday;
		DayViewFragment daySunday;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			d = new Date();
			d.setTime(currentDate);
			int dayNumber = d.getDay();
			if (dayNumber == 0) {
				dayNumber = 7;
			}

			monday = currentDate + ONE_DAY - dayNumber * ONE_DAY;
			tusday = currentDate + 2 * ONE_DAY - dayNumber * ONE_DAY;
			wednesday = currentDate + 3 * ONE_DAY - dayNumber * ONE_DAY;
			thersday = currentDate + 4 * ONE_DAY - dayNumber * ONE_DAY;
			friday = currentDate + 5 * ONE_DAY - dayNumber * ONE_DAY;
			saturday = currentDate + 6 * ONE_DAY - dayNumber * ONE_DAY;
			sunday = currentDate + 7 * ONE_DAY - dayNumber * ONE_DAY;

			dayMonday = DayViewFragment.newInstance(monday);
			dayTusday = DayViewFragment.newInstance(tusday);
			dayWednesday = DayViewFragment.newInstance(wednesday);
			dayThersday = DayViewFragment.newInstance(thersday);
			dayFriday = DayViewFragment.newInstance(friday);
			daySaturday = DayViewFragment.newInstance(saturday);
			daySunday = DayViewFragment.newInstance(sunday);

			Calendar cal = Calendar.getInstance(Locale.getDefault());
			cal.setTimeInMillis(sunday);
		}

		@Override
		public int getCount() {
			return 7;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return dayMonday;
			case 1:
				return dayTusday;
			case 2:
				return dayWednesday;
			case 3:
				return dayThersday;
			case 4:
				return dayFriday;
			case 5:
				return daySaturday;
			case 6:
				return daySunday;
			default:
				return null;
			}
		}
	}

	public boolean CreateMenu(Menu menu) {

		MenuItem settings = menu.add(R.string.menu_home).setIcon(
				R.drawable.ic_home);
		settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent homeIntent = new Intent(ViewPagerActivity.this,
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
				Intent translator = new Intent(ViewPagerActivity.this,
						TranslatorActivity.class);
				startActivity(translator);
				return false;
			}
		});
		MenuItem setings = menu.add(R.string.menu_settings).setIcon(
				R.drawable.ic_settings);
		setings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent i = new Intent(ViewPagerActivity.this,
						SettingsActivity.class);
				startActivity(i);
				return false;
			}
		});
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		return CreateMenu(menu);
	}

	@Override
	protected void onDestroy() {
		if (myTTS != null) {
			myTTS.stop();
			myTTS.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent homeIntent = new Intent(ViewPagerActivity.this,
					MainMenuActivity.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(homeIntent);
		}
		return (true);
	}

}
