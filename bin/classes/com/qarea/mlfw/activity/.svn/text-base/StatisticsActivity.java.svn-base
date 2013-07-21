package com.qarea.mlfw.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.DBHelper;
import com.qarea.mlfw.DBHelper.DBDictionary;
import com.qarea.mlfw.R;
import com.qarea.mlfw.adapter.StatisticWordExpandableListAdapter;
import com.qarea.mlfw.util.DateSlider;
import com.qarea.mlfw.util.Period;
import com.qarea.mlfw.util.SelectedDictionary;
import com.qarea.mlfw.util.WordStatistic;

public class StatisticsActivity extends BaseActivity implements OnInitListener,
		OnItemSelectedListener {
	private static int VOICE_CHECK_CODE = 0;
	private static final int DATE_DIALOG = 1;

	private Spinner period, sDictionary;
	private ArrayList<WordStatistic> wordsForSelectedDate;
	private ExpandableListView elvCheckingKnowladge;
	private Long fromDate, toDate;
	private int selected;
	public TextToSpeech myTts;

	private StatisticWordExpandableListAdapter adapterExpandable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		initialize();

		setVoice();
	}

	// voice installing
	private void setVoice() {
		myTts = new TextToSpeech(this, this);
	}

	// init text to speach
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			myTts.setLanguage(Locale.ENGLISH);
		} else if (status == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
		if (myTts.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE) {
			myTts.setLanguage(Locale.ENGLISH);
		}
	}

	@Override
	protected void onResume() {
		adapterExpandable = new StatisticWordExpandableListAdapter(this,
				wordsForSelectedDate, dataProvider, myTts);
		TextView noWords = (TextView) findViewById(R.id.statistic_no_words);
		if (wordsForSelectedDate.size() != 0) {
			elvCheckingKnowladge.setVisibility(View.VISIBLE);
			elvCheckingKnowladge.setAdapter(adapterExpandable);
			expandAll();
			noWords.setText("Choose period statistics:");
		} else {
			noWords.setText("You don't have words!");
			elvCheckingKnowladge.setVisibility(View.INVISIBLE);
		}
		super.onResume();
	}

	// method to collapse all groups
	private void expandAll() {
		int words_count = 0;
		for (int i = 0; i < adapterExpandable.getGroupCount(); i++) {
			words_count = adapterExpandable.getChildrenCount(i);
		}
		if (words_count != 0) {
			for (int i = 0; i < adapterExpandable.getGroupCount(); i++) {
				elvCheckingKnowladge.expandGroup(i);
			}
		}
	}

	private void initialize() {
		sDictionary = (Spinner) findViewById(R.id.sDictionary);
		period = (Spinner) findViewById(R.id.period_spinner);
		elvCheckingKnowladge = (ExpandableListView) findViewById(R.id.elvCheckingKnowladge);
		wordsForSelectedDate = new ArrayList<WordStatistic>();
		String[] list = new String[] { "By day", "By week", "By month",
				"By period" };
		ArrayAdapter<String> sadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		period.setAdapter(sadapter);
		period.setOnItemSelectedListener(this);
		period.setSelection(1);

		selected = 0;
		ArrayList<String> dictionaryArray = new ArrayList<String>();
		Cursor allDictionary = dataProvider.getSelectedDictionary();
		while (allDictionary.moveToNext())
			dictionaryArray.add(allDictionary.getString(allDictionary
					.getColumnIndex(DBDictionary.NAME)));
		allDictionary.close();
		ArrayAdapter<String> sadapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dictionaryArray);
		sadapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDictionary.setAdapter(sadapter1);
		sDictionary.setSelection(SelectedDictionary.getDictionaryList()
				.indexOf(SelectedDictionary.getDictionaryID()));
		sDictionary.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				TextView raw = (TextView) arg1;
				int dictionaryID = dataProvider.getDictionaryIdByName(raw
						.getText().toString());
				SelectedDictionary.setDictionaryID(dictionaryID);
				refreshList();

				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(SAVE_WORD, dictionaryID);
				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		getStatisticToSelectedDate(selected);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		selected = position;
		getStatisticToSelectedDate(selected);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	private void getStatisticToSelectedDate(int position) {

		((Button) findViewById(R.id.set_period_btn)).setVisibility(View.GONE);
		switch (position) {
		case 0:
			calendar.setTime(new Date());
			clearCalendar();
			fromDate = calendar.getTimeInMillis();
			calendar.add(Calendar.DAY_OF_WEEK, 1);
			toDate = calendar.getTimeInMillis();
			refreshList();
			expandAll();
			break;
		case 1:
			calendar.setTime(new Date());
			clearCalendar();
			calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
			fromDate = calendar.getTimeInMillis();
			clearCalendar();
			calendar.add(Calendar.DAY_OF_WEEK, 7);
			toDate = calendar.getTimeInMillis();

			refreshList();
			break;
		case 2:
			calendar.setTime(new Date());
			clearCalendar();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			fromDate = calendar.getTimeInMillis();
			clearCalendar();
			calendar.add(Calendar.MONTH, 1);
			toDate = calendar.getTimeInMillis();
			refreshList();
			break;
		case 3:
			showDialog(DATE_DIALOG);
			Button bPeriod = (Button) findViewById(R.id.set_period_btn);
			bPeriod.setVisibility(View.VISIBLE);
			bPeriod.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showDialog(DATE_DIALOG);

				}
			});
			break;
		}

	}

	private void refreshList() {
		Cursor allIdAndWords = dataProvider.getAllWordsIdAndName(fromDate,
				toDate);
		wordsForSelectedDate = new ArrayList<WordStatistic>();
		if (allIdAndWords != null) {

			float percent = 0;
			int allAttempts = 0;
			int id = -1;
			WordStatistic tmpWordStatistic;
			while (allIdAndWords.moveToNext()) {
				id = allIdAndWords.getInt(0);
				percent = 0;
				allAttempts = dataProvider
						.getCountCorrectOrAllAnswer(id, false);
				int correctAnswerCount = dataProvider
						.getCountCorrectOrAllAnswer(id, true);
				if (allAttempts != 0) {
					percent = ((float) correctAnswerCount / (float) allAttempts) * 100;
				}
				tmpWordStatistic = new WordStatistic(
						id,
						allIdAndWords.getString(allIdAndWords
								.getColumnIndex(DBHelper.DBWords.WORD_NAME)),
						percent,
						allAttempts,
						correctAnswerCount,
						(new Date()).getTime(),
						allIdAndWords.getInt(allIdAndWords
								.getColumnIndex(DBHelper.DBWords.DICTIONARY_ID)));
				wordsForSelectedDate.add(tmpWordStatistic);
			}
			Collections.sort(wordsForSelectedDate,
					new Comparator<WordStatistic>() {
						@Override
						public int compare(WordStatistic lhs, WordStatistic rhs) {
							return (int) (rhs.getPercent() * 100 - lhs
									.getPercent() * 100);
						}

					});
			allIdAndWords.close();
		} else {
		}
		onResume();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DATE_DIALOG) {
			final Period period = new Period(fromDate, toDate);
			DateSlider periodGetter = new DateSlider(StatisticsActivity.this,
					calendar, period);
			periodGetter.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					if (period.getFromDate() == fromDate
							&& period.getToDate() == toDate) {
						calendar.setTime(new Date());
						clearCalendar();
						fromDate = calendar.getTimeInMillis();
						calendar.add(Calendar.DAY_OF_WEEK, 1);
						toDate = calendar.getTimeInMillis();
					} else {
						fromDate = period.getFromDate();
						toDate = period.getToDate() + MILLSINDAY;
					}
					refreshList();
				}
			});
			return periodGetter;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onDestroy() {
		if (myTts != null) {
			myTts.stop();
			myTts.shutdown();
		}
		super.onDestroy();
	}
}
